package tools.validator;

import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import domain.AnalysisContext;
import domain.validation.ValidationResult;

public class LoopValidator implements Validator {
    private static final String FOR_DETECTED_ERR = "for문 사용 금지";
    private static final String WHILE_DETECTED_ERR = "while문 사용 금지";

    private static boolean isWhileDetected(AnalysisContext context) {
        // while, do-while 문 검증
        return !context.findAll(WhileStmt.class).isEmpty() || !context.findAll(DoStmt.class).isEmpty();
    }

    private static boolean isForDetected(AnalysisContext context) {
        // for, for-each 문 검증
        return !context.findAll(ForStmt.class).isEmpty() || !context.findAll(ForEachStmt.class).isEmpty();
    }

    @Override
    public ValidationResult validate(AnalysisContext context) {
        if (isForDetected(context)) {
            return ValidationResult.error(FOR_DETECTED_ERR);
        }
        if (isWhileDetected(context)) {
            return ValidationResult.error(WHILE_DETECTED_ERR);
        }
        return ValidationResult.ok();
    }
}
