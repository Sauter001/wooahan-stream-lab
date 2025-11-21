package tools.validator;

import domain.AnalysisContext;
import domain.validation.ValidationResult;
import domain.validation.strategy.ValidationStrategy;

import java.util.ArrayList;
import java.util.List;

public class CompositeValidatorImpl implements CompositeValidator {
    private final List<Validator> validators;
    private final ValidationStrategy strategy;

    public CompositeValidatorImpl(ValidationStrategy strategy) {
        this.validators = new ArrayList<>();
        this.strategy = strategy;
    }

    @Override
    public CompositeValidator add(Validator validator) {
        validators.add(validator);
        return this;
    }

    @Override
    public CompositeValidator remove(Validator validator) {
        validators.remove(validator);
        return this;
    }

    @Override
    public ValidationResult validate(AnalysisContext context) {
        List<ValidationResult> results = this.validators.stream()
                .map(v -> v.validate(context))
                .toList();

        return strategy.aggregate(results);
    }
}
