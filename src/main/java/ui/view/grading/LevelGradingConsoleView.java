package ui.view.grading;

import domain.validation.ValidationResult;
import tools.grader.level.LevelGrader;

public class LevelGradingConsoleView implements LevelGradingView {

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
    public void displayProblemHeader(String problemId, String methodName) {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("📝 문제 " + problemId + ": " + methodName);
        System.out.println("─".repeat(60));
    }

    @Override
    public void displayValidationResult(ValidationResult result) {
        if (result instanceof ValidationResult.Ok) {
            System.out.println("  ✓ 코드 구조 검증 통과");
        } else {
            System.out.println("  ✗ 규칙 위반 감지");
            printViolations(result);
        }
    }

    private void printViolations(ValidationResult result) {
        if (result instanceof ValidationResult.Violation violation) {
            System.out.println("    • " + violation.message());
        } else if (result instanceof ValidationResult.MultipleViolations multi) {
            for (ValidationResult.Violation v : multi.violations()) {
                System.out.println("    • " + v.message());
            }
        }
    }

    @Override
    public void displayTestStart() {
        System.out.println("\n  🧪 테스트 케이스 실행 중...");
    }

    @Override
    public void displayTestCaseResult(int testNum, boolean passed, Object input, Object expected, Object actual) {
        if (passed) {
            System.out.printf("    테스트 %d: ✅ 통과%n", testNum);
        } else {
            System.out.printf("    테스트 %d: ❌ 실패%n", testNum);
            System.out.printf("      입력:   %s%n", formatValue(input));
            System.out.printf("      기댓값: %s%n", formatValue(expected));
            System.out.printf("      실제값: %s%n", formatValue(actual));
        }
    }

    private String formatValue(Object value) {
        if (value == null) return "null";
        String str = value.toString();
        return str.length() > 100 ? str.substring(0, 100) + "..." : str;
    }

    @Override
    public void displayTestCaseError(int testNum, String errorMessage) {
        System.out.printf("    테스트 %d: ❌ 에러 - %s%n", testNum, errorMessage);
    }

    @Override
    public void displayProblemResult(LevelGrader.GradeResult gradeResult) {
        if (gradeResult.isComplete()) {
            System.out.println("  ✅ 문제 완료!");
        } else if (!gradeResult.isValid()) {
            System.out.println("  ❌ 규칙 위반으로 실패");
        } else {
            System.out.printf("  ⚠️  테스트 %d/%d 통과%n",
                gradeResult.passedTests(), gradeResult.totalTests());
        }
    }

    @Override
    public void displayLevelSummary(int totalProblems, int passedProblems) {
        System.out.println("\n" + "=".repeat(60));
        System.out.printf("📊 레벨 진행률: %d / %d 문제 완료%n", passedProblems, totalProblems);

        if (passedProblems == totalProblems) {
            System.out.println("🎉 레벨 완료! 다음 레벨로 진행할 수 있습니다.");
        }
        System.out.println("=".repeat(60));
    }
}
