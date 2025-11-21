package tools.validator;

import com.github.javaparser.ast.expr.MethodCallExpr;
import domain.AnalysisContext;
import domain.validation.ValidationResult;

public class StreamUsageValidator implements Validator {
    private static final String STREAM_MUST_USED_ERR = "Stream API 사용 필수";

    @Override
    public ValidationResult validate(AnalysisContext context) {
        boolean hasStream = detectStream(context);

        if (!hasStream) {
            return ValidationResult.error(STREAM_MUST_USED_ERR);
        }
        return ValidationResult.ok();
    }

    private boolean detectStream(AnalysisContext context) {
        return context.findAll(MethodCallExpr.class)
                .stream().anyMatch(call -> call.getNameAsString().equals("stream"));
    }
}
