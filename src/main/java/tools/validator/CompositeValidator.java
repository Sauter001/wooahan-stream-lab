package tools.validator;

public interface CompositeValidator extends  Validator {
    CompositeValidator add(Validator validator);
    CompositeValidator remove(Validator validator);
}
