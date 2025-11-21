package ui.view.grading;

import domain.validation.ValidationResult;
import tools.grader.TutorialGrader;

import java.util.List;

public interface GradingView {
    /**
     * 파일 변경 감지 메시지 출력
     */
    void displayFileChangeDetected();

    /**
     * 채점 시작 메시지 출력
     */
    void displayGradingStart(String fileName);

    /**
     * 규칙 검증 결과 출력
     */
    void displayRuleValidation(ValidationResult result);

    /**
     * 테스트 실행 시작 메시지 출력
     */
    void displayTestStart();

    /**
     * 최종 채점 결과 출력
     */
    void displayFinalResult(TutorialGrader.GradeResult gradeResult, int passedTests, int totalTests);

    /**
     * 개별 메서드 채점 헤더 출력
     */
    void displayMethodHeader(String methodName, String description);

    /**
     * 개별 테스트 케이스 결과 출력
     */
    void displayTestCaseResult(int testNum, boolean passed, List<Integer> input, List<Integer> expected, List<Integer> actual);

    /**
     * 테스트 케이스 에러 출력
     */
    void displayTestCaseError(int testNum, String errorMessage);
}
