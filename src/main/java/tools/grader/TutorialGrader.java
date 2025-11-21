package tools.grader;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import domain.AnalysisContext;
import domain.validation.ValidationResult;
import tools.validator.Validator;

import java.io.File;

public class TutorialGrader {
    private final Validator validator;
    private final int maxScore;

    public TutorialGrader(Validator validator, int maxScore) {
        this.validator = validator;
        this.maxScore = maxScore;
    }

    public GradeResult gradeMethod(File sourceFile, String methodName) {
        try {
            JavaParser parser = new JavaParser();
            CompilationUnit cu = parser.parse(sourceFile).getResult().orElseThrow();
            AnalysisContext context = new AnalysisContext(cu);

            ValidationResult result = validator.validate(context);

            return new GradeResult(result, maxScore);
        } catch (Exception e) {
            return new GradeResult(
                ValidationResult.error("파일을 읽을 수 없습니다: " + e.getMessage()),
                maxScore
            );
        }
    }

    public static class GradeResult {
        private final ValidationResult validationResult;
        private final int maxScore;

        public GradeResult(ValidationResult validationResult, int maxScore) {
            this.validationResult = validationResult;
            this.maxScore = maxScore;
        }

        public boolean isValid() {
            return validationResult instanceof ValidationResult.Ok;
        }

        public ValidationResult validationResult() {
            return validationResult;
        }

        public int maxScore() {
            return maxScore;
        }
    }
}
