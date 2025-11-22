package ui.view.grading;

import domain.validation.ValidationResult;
import tools.grader.level.LevelGrader;

/**
 * Level 채점 결과 표시용 View 인터페이스
 */
public interface LevelGradingView {
    void displayFileChangeDetected();
    void displayGradingStart(String fileName);
    void displayProblemHeader(String problemId, String methodName);
    void displayValidationResult(ValidationResult result);
    void displayTestStart();
    void displayTestCaseResult(int testNum, boolean passed, Object input, Object expected, Object actual);
    void displayTestCaseError(int testNum, String errorMessage);
    void displayProblemResult(LevelGrader.GradeResult gradeResult);
    void displayLevelSummary(int totalProblems, int passedProblems);
}
