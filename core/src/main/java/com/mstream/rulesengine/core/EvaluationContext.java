package com.mstream.rulesengine.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class EvaluationContext {

    private static final EvaluationContext INITIAL =
            new EvaluationContext(Collections.emptyMap());

    private final Map<String, Boolean> gatheredFacts;

    public static EvaluationContext initial() {
        return INITIAL;
    }

    public static EvaluationContext fromResult(EvaluationResult result, Map<String, Boolean> newFacts) {
        if (result.isFinished()) {
            throw new IllegalArgumentException("the evaluation is finished");
        }

        Set<String> newFactNames = newFacts.keySet();

        if (!newFactNames.containsAll(result.getRequestedFacts())) {
            throw new IllegalArgumentException("not all requested facts have been provided");
        }

        if (result.getGatheredFacts().keySet().stream().anyMatch(newFactNames::contains)) {
            throw new IllegalArgumentException("at least one new fact overrides already existing one");
        }

        Map<String, Boolean> facts = new HashMap<>(result.getGatheredFacts());
        facts.putAll(newFacts);

        return new EvaluationContext(facts);
    }

    private EvaluationContext( Map<String, Boolean> gatheredFacts) {
        this.gatheredFacts = gatheredFacts;
    }

    public Map<String, Boolean> getGatheredFacts() {
        return gatheredFacts;
    }

    @Override
    public String toString() {
        return "EvaluationContext{" +
                "gatheredFacts=" + gatheredFacts +
                '}';
    }
}
