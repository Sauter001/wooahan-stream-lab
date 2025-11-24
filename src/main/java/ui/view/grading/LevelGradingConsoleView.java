package ui.view.grading;

import domain.validation.ValidationResult;
import tools.grader.level.LevelGrader;

import java.util.List;

public class LevelGradingConsoleView implements LevelGradingView {

    @Override
    public void displayFileChangeDetected() {
        System.out.println("\n⏳ 파일 변경 감지, 완료 대기 중...");
    }

    @Override

    public void displayGradingStart(String fileName) {
        // 컴팩트 모드에서는 간단하게만 표시
    }

    @Override
    public void displayCompactResults(int level, List<ProblemGradeResult> results) {
        int total = results.size();
        int passed = (int) results.stream().filter(ProblemGradeResult::isComplete).count();

        // 진행바 생성
        StringBuilder progressBar = new StringBuilder("[");
        ProblemGradeResult firstFailed = null;

        for (ProblemGradeResult result : results) {
            if (result.isComplete()) {
                progressBar.append("✅");
            } else {
                if (firstFailed == null) {
                    firstFailed = result;
                }
                progressBar.append("❌");
            }
        }
        progressBar.append("]");

        // 레벨 + 진행바 + 진행률 출력
        System.out.printf("%n🔄 Level %d %s %d/%d%n", level, progressBar, passed, total);

        // 첫 번째 실패한 문제 상세 정보
        if (firstFailed != null) {
            System.out.println();
            System.out.printf("❌ %s %s%n", firstFailed.getProblemId(), firstFailed.getMethodName());
            System.out.printf("   \"%s\"%n", firstFailed.getDescription());

            // 코드 위반 시 추가 정보
            LevelGrader.GradeResult gradeResult = firstFailed.gradeResult();
            if (!gradeResult.isValid()) {
                printViolations(gradeResult.validationResult());
            }
        }

        // 레벨 완료 시 축하 메시지
        if (passed == total) {
            System.out.println();
            System.out.println("🎉 레벨 완료! 다음 레벨로 진행할 수 있습니다.");
        }

        System.out.println();
    }

    private void printViolations(ValidationResult result) {
        if (result instanceof ValidationResult.Violation violation) {
            System.out.println("   ⚠️ " + violation.message());
        } else if (result instanceof ValidationResult.MultipleViolations multi) {
            for (ValidationResult.Violation v : multi.violations()) {
                System.out.println("   ⚠️ " + v.message());
            }
        }
    }
}
