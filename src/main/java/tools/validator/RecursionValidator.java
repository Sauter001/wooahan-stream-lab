package tools.validator;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import domain.AnalysisContext;
import domain.validation.ValidationResult;

import java.util.Iterator;

public class RecursionValidator implements Validator {
    private static final String RECURSION_PROHIBITED_ERR = "재귀 호출 금지";

    @Override
    public ValidationResult validate(AnalysisContext context) {
        Iterator<MethodDeclaration> methodIterator = context.getMethodIterator();
        while (methodIterator.hasNext()) {
            MethodDeclaration method = methodIterator.next();
            boolean hasRecursion = detectRecursion(method);

            if  (hasRecursion) {
                return ValidationResult.error(RECURSION_PROHIBITED_ERR);
            }
        }

        return ValidationResult.ok();
    }

    private boolean detectRecursion(MethodDeclaration method) {
        String methodName = method.getNameAsString();
        return method.findAll(MethodCallExpr.class)
                .stream()
                .anyMatch(call -> methodName.equals(call.getNameAsString()));
    }
}
