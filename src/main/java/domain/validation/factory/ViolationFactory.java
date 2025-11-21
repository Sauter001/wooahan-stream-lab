package domain.validation.factory;

import domain.validation.strategy.CollectAllStrategy;
import tools.validator.*;

public class ViolationFactory {
    // tutorial, Level 1, 2, 3에 적용되는 제약
    public static CompositeValidator createStrictestValidator() {
        return new CompositeValidatorImpl(new CollectAllStrategy())
                .add(new LoopValidator())
                .add(new RecursionValidator())
                .add(new StreamUsageValidator())
                .add(new BlockLambdaValidator());
    }

    // Level 4, 5에 적용되는 제약
    public static CompositeValidator createBlockLambdaAllowedValidator() {
        return new CompositeValidatorImpl(new CollectAllStrategy())
                .add(new LoopValidator())
                .add(new RecursionValidator())
                .add(new StreamUsageValidator());
    }

    public static CompositeValidator createSecretPhaseValidator() {
        return new CompositeValidatorImpl(new CollectAllStrategy())
                .add(new LoopValidator())
                .add(new RecursionValidator());
    }
}
