package tools.validator;

import com.github.javaparser.ast.expr.LambdaExpr;
import domain.AnalysisContext;
import domain.validation.ValidationResult;

public class BlockLambdaValidator implements Validator {
    @Override
    public ValidationResult validate(AnalysisContext context) {
        boolean hasBlockLambda = context.findAll(LambdaExpr.class)
                .stream()
                .anyMatch(lambda -> lambda.getBody().isBlockStmt());

        if (!hasBlockLambda) {
            return ValidationResult.error("블록 대신 표현식 하나로 사용하기");
        }

        return ValidationResult.ok();
    }
}
