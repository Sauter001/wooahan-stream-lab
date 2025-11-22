package tools.validator;

import com.github.javaparser.ast.body.VariableDeclarator;
import domain.AnalysisContext;
import domain.validation.ValidationResult;

import java.util.List;

/**
 * 변수 선언 수를 제한하는 Validator.
 * maxVariables 개수까지의 변수 선언을 허용합니다.
 */
public class MaxVariableDeclarationValidator implements Validator {
    private static final String VARIABLE_LIMIT_EXCEEDED_ERR = "변수 선언 개수 초과";
    private final int maxVariables;

    public MaxVariableDeclarationValidator(int maxVariables) {
        this.maxVariables = maxVariables;
    }

    @Override
    public ValidationResult validate(AnalysisContext context) {
        List<VariableDeclarator> variables = context.findAll(VariableDeclarator.class);
        int variableCount = variables.size();

        if (variableCount > maxVariables) {
            String variableNames = variables.stream()
                    .map(VariableDeclarator::getNameAsString)
                    .limit(3)
                    .toList()
                    .toString();

            return ValidationResult.error(
                    String.format("%s: 최대 %d개 허용, %d개 발견 %s",
                            VARIABLE_LIMIT_EXCEEDED_ERR, maxVariables, variableCount, variableNames)
            );
        }

        return ValidationResult.ok();
    }
}
