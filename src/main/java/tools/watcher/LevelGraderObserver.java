package tools.watcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import context.GameContext;
import domain.Profile;
import repository.ProfileRepository;
import tools.grader.level.LevelGrader;
import tools.grader.level.LevelTestData;
import ui.view.grading.LevelGradingView;
import ui.view.grading.LevelGradingView.ProblemGradeResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LevelGraderObserver implements GraderObserver {
    private static final String SOLUTION_CLASS_PATTERN = "solutions.level%d.Level%d";
    private static final String TEST_DATA_PATTERN = "data/test-data/level%d/level%d.json";
    private static final String LEVEL_FILE_PATTERN = "Level%d.java";
    private static final long DEBOUNCE_DELAY_SECONDS = 2;
    private static final List<Integer> FIBONACCI_7 = List.of(1, 1, 2, 3, 5, 8, 13);

    private final int level;
    private final LevelTestData testData;
    private final LevelGradingView view;
    private final GameContext gameContext;
    private final ProfileRepository profileRepository;
    private final ScheduledExecutorService scheduler;
    private final String expectedFileName;
    private final AtomicBoolean isGrading = new AtomicBoolean(false);
    private ScheduledFuture<?> pendingGrade;
    private ClassLoader previousClassLoader = null;

    public LevelGraderObserver(int level, LevelGradingView view, GameContext gameContext, ProfileRepository profileRepository) {
        this.level = level;
        this.view = view;
        this.gameContext = gameContext;
        this.profileRepository = profileRepository;
        this.expectedFileName = String.format(LEVEL_FILE_PATTERN, level);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String testDataPath = String.format(TEST_DATA_PATTERN, level, level);
            this.testData = mapper.readValue(new File(testDataPath), LevelTestData.class);

            this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread thread = new Thread(r, "LevelGraderScheduler-" + level);
                thread.setDaemon(true);
                return thread;
            });
        } catch (Exception e) {
            throw new RuntimeException("LevelGraderObserver 초기화 실패 (Level " + level + ")", e);
        }
    }

    @Override
    public void onFileChanged(Path filePath) {
        String fileName = filePath.getFileName().toString();

        if (!fileName.equals(expectedFileName)) {
            return;
        }

        if (pendingGrade != null && !pendingGrade.isDone()) {
            pendingGrade.cancel(false);
        }

        view.displayFileChangeDetected();

        pendingGrade = scheduler.schedule(() -> {
            executeGrading(filePath);
        }, DEBOUNCE_DELAY_SECONDS, TimeUnit.SECONDS);
    }

    private void executeGrading(Path filePath) {
        if (!isGrading.compareAndSet(false, true)) {
            return;
        }

        try {
            view.displayGradingStart(filePath.getFileName().toString());

            // 소스 파일 컴파일
            if (!compileSource()) {
                System.err.println("❌ 컴파일 실패. 문법 오류를 확인하세요.");
                return;
            }

            Class<?> solutionClass = loadSolutionClass();
            File sourceFile = filePath.toFile();

            LevelGrader grader = new LevelGrader(testData);

            // 모든 문제 채점 결과 수집
            List<ProblemGradeResult> results = new ArrayList<>();

            for (LevelTestData.Problem problem : testData.getProblems()) {
                LevelGrader.GradeResult gradeResult = grader.gradeProblem(problem, sourceFile, solutionClass);
                results.add(new ProblemGradeResult(problem, gradeResult));
            }

            // 컴팩트 결과 출력
            view.displayCompactResults(level, results);

            // 레벨 완료 체크
            long passedCount = results.stream().filter(ProblemGradeResult::isComplete).count();
            if (passedCount == results.size()) {
                gameContext.setLevelCompleted(level, true);
            }

            // Level 5는 항상 Secret Phase 해금 체크 (나중에 구현해도 해금 가능)
            if (level == 5) {
                checkSecretPhaseUnlock(solutionClass);
            }

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Level" + level + " 클래스를 찾을 수 없습니다. 프로젝트가 컴파일되었는지 확인하세요.");
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("컴파일")) {
                System.err.println("❌ " + msg);
            } else {
                System.err.println("❌ 채점 중 오류 발생: " + msg);
            }
        } finally {
            isGrading.set(false);
        }
    }

    private boolean compileSource() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String gradleCommand = os.contains("win") ? "gradlew.bat" : "./gradlew";

            ProcessBuilder pb = new ProcessBuilder(gradleCommand, "compileJava", "-q");
            pb.redirectErrorStream(true);
            pb.directory(new File(System.getProperty("user.dir")));

            Process process = pb.start();

            // 컴파일 에러 출력 읽기
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.err.println(output.toString().trim());
                return false;
            }

            return true;
        } catch (Exception e) {
            System.err.println("❌ 컴파일 프로세스 실행 실패: " + e.getMessage());
            return false;
        }
    }

    private Class<?> loadSolutionClass() throws Exception {
        cleanUpClassLoader();

        String className = String.format(SOLUTION_CLASS_PATTERN, level, level);
        String classFilePath = className.replace('.', '/') + ".class";

        Path classesDir = Paths.get("build/classes/java/main").toAbsolutePath();
        Path classFile = classesDir.resolve(classFilePath);

        // .class 파일 바이트를 직접 읽어서 캐싱 문제 방지
        byte[] classBytes = Files.readAllBytes(classFile);

        previousClassLoader = new ByteArrayClassLoader(getClass().getClassLoader(), className, classBytes);
        return previousClassLoader.loadClass(className);
    }

    private void cleanUpClassLoader() {
        // ByteArrayClassLoader는 close가 필요 없음
        previousClassLoader = null;
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

    @SuppressWarnings("unchecked")
    private void checkSecretPhaseUnlock(Class<?> solutionClass) {
        try {
            Method doNotTouchMethod = solutionClass.getMethod("doNotTouch");
            Object result = doNotTouchMethod.invoke(null);

            if (result instanceof List<?> list && list.equals(FIBONACCI_7)) {
                Profile profile = profileRepository.load().orElse(null);
                if (profile != null && !profile.isSecretUnlocked()) {
                    profile.unlockSecret();
                    profileRepository.save(profile);

                    // 해금 연출
                    System.out.println();
                    System.out.println("██████████████████████████████████████");
                    System.out.println("█                                    █");
                    System.out.println("█   ░░░ HIDDEN SEQUENCE DETECTED ░░░ █");
                    System.out.println("█                                    █");
                    System.out.println("██████████████████████████████████████");
                    System.out.println();
                    System.out.println("🔓 Secret Phase 해금!");
                    System.out.println();
                }
            }
        } catch (Exception e) {
            // doNotTouch 메서드 호출 실패 시 무시
        }
    }
}
