package com.mstream.rulesengine.core;

public interface RulesEngine {

    ValidationResult validate(ValidationOptions options);

    EvaluationResult evaluate(EvaluationContext context);
}
