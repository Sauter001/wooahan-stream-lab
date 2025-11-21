package domain.validation.strategy;

import domain.validation.ValidationResult;

import java.util.List;

/**
 * 정적 분석에서 하나라도 코드 제약을 만족 못하는 경우
 */
public class FastFailStrategy implements ValidationStrategy {
    @Override
    public ValidationResult aggregate(List<ValidationResult> results) {
        return results.stream()
                .filter(r -> r instanceof ValidationResult.Violation)
                .findFirst()
                .orElse(ValidationResult.ok());
    }
}
