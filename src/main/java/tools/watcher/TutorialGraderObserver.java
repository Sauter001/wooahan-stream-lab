package tools.watcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import context.GameContext;
import domain.validation.factory.ViolationFactory;
import tools.grader.TutorialGrader;
import tools.grader.TutorialTestData;
import tools.validator.CompositeValidator;
import tools.validator.NoVariableDeclarationValidator;
import tools.validator.SingleStatementValidator;
import ui.view.grading.GradingView;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TutorialGraderObserver implements GraderObserver {
    private static final String TUTORIAL_CLASS = "solutions.tutorial.Tutorial";
    private static final String TEST_DATA_PATH = "data/test-data/tutorial/tutorial.json";
    private static final long DEBOUNCE_DELAY_SECONDS = 2;
    public static final String TUTORIAL_JAVA_FILE = "Tutorial.java";

    private final TutorialTestData testData;
    private final CompositeValidator validator;
    private final ScheduledExecutorService scheduler;
    private final GradingView view;
    private final GameContext gameContext;
    private ScheduledFuture<?> pendingGrade;
    private final AtomicBoolean isGrading = new AtomicBoolean(false);

    public TutorialGraderObserver(GradingView view, GameContext gameContext) {
        this.view = view;
        this.gameContext = gameContext;
        try {
            // 테스트 데이터 로드
            ObjectMapper mapper = new ObjectMapper();
            this.testData = mapper.readValue(new File(TEST_DATA_PATH), TutorialTestData.class);

            // Validator 생성
            this.validator = ViolationFactory.createStrictestValidator()
                    .add(new SingleStatementValidator())
                    .add(new NoVariableDeclarationValidator());

            // Debouncing용 스케줄러 생성
            this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread thread = new Thread(r, "GraderScheduler");
                thread.setDaemon(true);
                return thread;
            });
        } catch (Exception e) {
            throw new RuntimeException("TutorialGraderObserver 초기화 실패", e);
        }
    }

    @Override
    public void onFileChanged(Path filePath) {
        String fileName = filePath.getFileName().toString();

        // Tutorial.java만 처리
        if (!fileName.equals(TUTORIAL_JAVA_FILE)) {
            return;
        }

        // 이전 대기 중인 채점이 있으면 취소
        if (pendingGrade != null && !pendingGrade.isDone()) {
            pendingGrade.cancel(false);
        }

        view.displayFileChangeDetected();

        // Debounce 지연 후 채점 예약
        pendingGrade = scheduler.schedule(() -> {
            executeGrading(filePath);
        }, DEBOUNCE_DELAY_SECONDS, TimeUnit.SECONDS);
    }

    private void executeGrading(Path filePath) {
        if (!isGrading.compareAndSet(false, true)) {
            return;
        }

        view.displayGradingStart(filePath.getFileName().toString());

        int totalMethods = testData.getMethods().size();
        int completedMethods = 0;

        // 각 메서드 채점
        for (TutorialTestData.MethodTest methodTest : testData.getMethods()) {
            if (gradeMethod(methodTest, filePath.toFile())) {
                completedMethods++;
            }
        }

        // 모든 메서드가 완료되면 튜토리얼 완료 표시
        if (completedMethods == totalMethods) {
            gameContext.setTutorialCompleted(true);
            System.out.println("\n🎉 튜토리얼을 모두 완료했습니다! 메인 메뉴로 이동하려면 'exit'를 입력하세요.");
        }

        isGrading.set(false);
    }

    private boolean gradeMethod(TutorialTestData.MethodTest methodTest, File sourceFile) {
        view.displayMethodHeader(methodTest.getName(), methodTest.getDescription());

        try {
            // Grader 생성
            TutorialGrader grader = new TutorialGrader(validator, 5);

            // 코드 구조 검증
            TutorialGrader.GradeResult gradeResult = grader.gradeMethod(sourceFile, methodTest.getName());

            // 검증 실패 시 에러 표시하고 테스트 건너뛰기
            if (!gradeResult.isValid()) {
                view.displayFinalResult(gradeResult, 0, methodTest.getTestCases().size());
                return false;
            }

            // 테스트 케이스 실행
            int passedTests = runTestCases(methodTest);
            int totalTests = methodTest.getTestCases().size();

            // 결과 표시
            view.displayFinalResult(gradeResult, passedTests, totalTests);

            // 모든 테스트 통과 시 true 반환
            return passedTests == totalTests;

        } catch (Exception e) {
            System.err.println("❌ 메서드 채점 중 오류 발생 " + methodTest.getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private int runTestCases(TutorialTestData.MethodTest methodTest) {
        int passed = 0;
        int testNum = 1;

        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Class<?> tutorialClass = classLoader.loadClass(TUTORIAL_CLASS);
            Method method = tutorialClass.getMethod(methodTest.getName(), List.class);

            view.displayTestStart();

            for (TutorialTestData.TestCase testCase : methodTest.getTestCases()) {
                try {
                    @SuppressWarnings("unchecked")
                    List<Integer> actual = (List<Integer>) method.invoke(null, testCase.getInput());

                    boolean testPassed = actual.equals(testCase.getExpected());
                    view.displayTestCaseResult(testNum, testPassed, testCase.getInput(), testCase.getExpected(), actual);

                    if (testPassed) {
                        passed++;
                    }
                } catch (Exception e) {
                    String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                    view.displayTestCaseError(testNum, errorMessage);
                }
                testNum++;
            }

            System.out.println();
        } catch (ClassNotFoundException e) {
            System.err.println("❌ 오류: Tutorial 클래스를 찾을 수 없습니다. 프로젝트가 컴파일되었는지 확인하세요.");
        } catch (Exception e) {
            System.err.println("❌ 테스트 케이스 실행 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return passed;
    }

    public void shutdown() {
        if (pendingGrade != null && !pendingGrade.isDone()) {
            pendingGrade.cancel(false);
        }
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
