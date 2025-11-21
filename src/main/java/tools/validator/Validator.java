package tools.validator;

import domain.AnalysisContext;
import domain.validation.ValidationResult;

public interface Validator {
    ValidationResult validate(AnalysisContext context);
}
