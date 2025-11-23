package tools.validator;

import com.github.javaparser.ast.expr.MethodCallExpr;
import domain.AnalysisContext;
import domain.validation.ValidationResult;

import java.util.Set;

public class StreamUsageValidator implements Validator {
    private static final String STREAM_MUST_USED_ERR = "Stream API 사용 필수";

    // Stream을 생성하는 메서드들
    private static final Set<String> STREAM_CREATION_METHODS = Set.of(
            "stream",           // Collection.stream()
            "parallelStream",   // Collection.parallelStream()
            "of",               // Stream.of(), IntStream.of()
            "range",            // IntStream.range()
            "rangeClosed",      // IntStream.rangeClosed()
            "iterate",          // Stream.iterate()
            "generate",         // Stream.generate()
            "concat",           // Stream.concat()
            "empty",            // Stream.empty()
            "chars",            // String.chars() -> IntStream
            "codePoints",       // String.codePoints() -> IntStream
            "lines"             // String.lines(), BufferedReader.lines()
    );

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
                .stream()
                .anyMatch(call -> STREAM_CREATION_METHODS.contains(call.getNameAsString()));
    }
}
