package ui.view.grading;

import domain.validation.ValidationResult;
import tools.grader.TutorialGrader;

import java.util.List;

public class GradingConsoleView implements GradingView {

    @Override
    public void displayFileChangeDetected() {
        System.out.println("\n⏳ 파일 변경 감지, 완료 대기 중...");
    }

    @Override
    public void displayGradingStart(String fileName) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔄 채점 시작: " + fileName);
        System.out.println("=".repeat(60));
    }

    @Override
    public void displayRuleValidation(ValidationResult result) {
        System.out.println("\n규칙 검증 중...");

        if (result instanceof ValidationResult.Ok) {
            System.out.println("✓ 루프 없음");
            System.out.println("✓ 재귀 없음");
            System.out.println("✓ 헬퍼 메소드 없음");
            System.out.println("✓ 블록 람다 없음");
            System.out.println("✓ 컬렉션 선언 개수 적합");
        } else {
            System.out.println("✗ 규칙 위반 감지");
        }
    }

    @Override
    public void displayTestStart() {
        System.out.println("\n🧪 테스트 케이스 실행 중...\n");
    }

    @Override
    public void displayFinalResult(TutorialGrader.GradeResult gradeResult, int passedTests, int totalTests) {
        System.out.println("\n" + "=".repeat(60));

        if (!gradeResult.isValid()) {
            System.out.println("❌ 규칙 위반으로 0점 처리되었습니다.");
            System.out.println();

            // 위반 내용 출력
            printViolations(gradeResult.validationResult());

            System.out.println();
            System.out.println("점수: 0 / " + gradeResult.maxScore() + " 점");
        } else {
            int score = passedTests == totalTests ? gradeResult.maxScore() : (gradeResult.maxScore() * passedTests / totalTests);

            if (passedTests == totalTests) {
                System.out.println("✅ 모든 테스트를 통과했습니다!");
            } else {
                System.out.println("⚠️  일부 테스트가 실패했습니다.");
            }

            System.out.println("테스트 케이스: " + passedTests + " / " + totalTests + " 통과");
            System.out.println("점수: " + score + " / " + gradeResult.maxScore() + " 점");
        }

        System.out.println("=".repeat(60));
    }

    private void printViolations(ValidationResult result) {
        if (result instanceof ValidationResult.Violation violation) {
            System.out.println("  • " + violation.message());
        } else if (result instanceof ValidationResult.MultipleViolations multi) {
            for (ValidationResult.Violation v : multi.violations()) {
                System.out.println("  • " + v.message());
            }
        }
    }

    @Override
    public void displayMethodHeader(String methodName, String description) {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("📝 채점 메서드: " + methodName);
        System.out.println("설명: " + description);
        System.out.println("─".repeat(60) + "\n");
    }

    @Override
    public void displayTestCaseResult(int testNum, boolean passed, List<Integer> input, List<Integer> expected, List<Integer> actual) {
        if (passed) {
            System.out.printf("  테스트 %d: ✅ 통과%n", testNum);
        } else {
            System.out.printf("  테스트 %d: ❌ 실패%n", testNum);
            System.out.printf("    입력:    %s%n", input);
            System.out.printf("    기댓값: %s%n", expected);
            System.out.printf("    실제값:   %s%n", actual);
        }
    }

    @Override
    public void displayTestCaseError(int testNum, String errorMessage) {
        System.out.printf("  테스트 %d: ❌ 에러 - %s%n", testNum, errorMessage);
    }
}
