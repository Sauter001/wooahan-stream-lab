package tools.validator;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import domain.AnalysisContext;
import domain.validation.ValidationResult;

import java.util.List;

public class SingleStatementValidator implements Validator {
    private static final String MULTIPLE_STATEMENTS_ERR = "메서드는 정확히 하나의 return 문만 포함해야 합니다";

    @Override
    public ValidationResult validate(AnalysisContext context) {
        List<MethodDeclaration> methods = context.findAll(MethodDeclaration.class);

        for (MethodDeclaration method : methods) {
            if (method.getBody().isEmpty()) {
                continue;
            }

            BlockStmt body = method.getBody().get();
            List<Statement> statements = body.getStatements();

            // 빈 라인이나 주석을 제외한 실제 statement 개수 확인
            long actualStatements = statements.stream()
                    .filter(stmt -> !stmt.isEmptyStmt())
                    .count();

            if (actualStatements != 1) {
                return ValidationResult.error(
                    String.format("%s (현재: %d개)", MULTIPLE_STATEMENTS_ERR, actualStatements)
                );
            }
        }

        return ValidationResult.ok();
    }
}
