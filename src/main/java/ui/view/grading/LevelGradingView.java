package ui.view.grading;

import tools.grader.level.LevelGrader;
import tools.grader.level.LevelTestData;

import java.util.List;

/**
 * Level 채점 결과 표시용 View 인터페이스
 */
public interface LevelGradingView {
    void displayFileChangeDetected();
    void displayGradingStart(String fileName);

    /**
     * 컴팩트한 진행바 스타일로 전체 채점 결과 표시
     */
    void displayCompactResults(
            int level,
            List<ProblemGradeResult> results
    );

    /**
     * 문제별 채점 결과를 담는 레코드
     */
    record ProblemGradeResult(
            LevelTestData.Problem problem,
            LevelGrader.GradeResult gradeResult
    ) {
        public boolean isComplete() {
            return gradeResult.isComplete();
        }

        public String getProblemId() {
            return problem.getId();
        }

        public String getMethodName() {
            return problem.getMethodName();
        }

        public String getDescription() {
            return problem.getDescription();
        }
    }
}
