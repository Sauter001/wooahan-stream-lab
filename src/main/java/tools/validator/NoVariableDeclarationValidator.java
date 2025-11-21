package tools.validator;

import com.github.javaparser.ast.body.VariableDeclarator;
import domain.AnalysisContext;
import domain.validation.ValidationResult;

import java.util.List;

public class NoVariableDeclarationValidator implements Validator {
    private static final String VARIABLE_DECLARATION_ERR = "변수 선언을 사용하지 마세요. Stream 체이닝만 사용해야 합니다";

    @Override
    public ValidationResult validate(AnalysisContext context) {
        List<VariableDeclarator> variables = context.findAll(VariableDeclarator.class);

        if (!variables.isEmpty()) {
            String firstVar = variables.get(0).getNameAsString();
            return ValidationResult.error(
                String.format("%s (발견된 변수: %s)", VARIABLE_DECLARATION_ERR, firstVar)
            );
        }

        return ValidationResult.ok();
    }
}
