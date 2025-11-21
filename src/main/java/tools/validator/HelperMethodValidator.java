package tools.validator;

import com.github.javaparser.ast.body.MethodDeclaration;
import domain.AnalysisContext;
import domain.validation.ValidationResult;

public class HelperMethodValidator implements Validator {
    private static final String NO_HELPER_METHOD_ALLOWED_ERR = "메소드 추가 선언 금지";
    private final int maxHelperMethods;

    public HelperMethodValidator(int maxHelperMethods) {
        this.maxHelperMethods = maxHelperMethods;
    }

    @Override
    public ValidationResult validate(AnalysisContext context) {
        long helperCount = countMethods(context);

        if (helperCount > maxHelperMethods) {
            return ValidationResult.error(NO_HELPER_METHOD_ALLOWED_ERR);
        }
        return ValidationResult.ok();
    }

    private long countMethods(AnalysisContext context) {
        return context.findAll(MethodDeclaration.class).size();
    }
}
