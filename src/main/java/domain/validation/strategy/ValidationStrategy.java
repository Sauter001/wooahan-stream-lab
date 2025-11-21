package domain.validation.strategy;

import domain.validation.ValidationResult;

import java.util.List;

public interface ValidationStrategy {
    ValidationResult aggregate(List<ValidationResult> results);
}
