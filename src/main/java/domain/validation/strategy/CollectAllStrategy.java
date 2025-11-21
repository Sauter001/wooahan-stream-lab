package domain.validation.strategy;

import domain.validation.ValidationResult;

import java.util.List;

public class CollectAllStrategy implements ValidationStrategy {
    @Override
    public ValidationResult aggregate(List<ValidationResult> results) {
        List<ValidationResult.Violation> violations = results.stream()
                .filter(r -> r instanceof ValidationResult.Violation)
                .map(r -> (ValidationResult.Violation) r)
                .toList();

        if (violations.isEmpty()) {
            return ValidationResult.ok();
        }
        return ValidationResult.multipleViolations(violations);
    }
}
