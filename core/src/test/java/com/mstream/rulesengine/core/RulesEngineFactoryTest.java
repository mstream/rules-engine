package com.mstream.rulesengine.core;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.*;

public class RulesEngineFactoryTest {

    private static final String RULES = "[" +
            "{" +
            ":name \"rule1\" " +
            ":conditions [{\"fact1\" true}] " +
            ":actions [\"action1\"] " +
            "}" +
            " " +
            "{" +
            ":name \"rule2\" " +
            ":conditions [{\"fact1\" false}] " +
            ":actions [\"action2\"] " +
            "}" +
            "]";


    @Test
    public void test() {
        RulesEngine rulesEngine = RulesEngineFactory.fromEdn(RULES);
        ValidationResult validationResult = rulesEngine.validate(new ValidationOptions());
        assertEquals(Collections.singleton("fact1"), validationResult.getFacts());
        assertEquals(new HashSet<>(Arrays.asList("action1", "action2")), validationResult.getActions());
        assertTrue(validationResult.getErrors().isEmpty());
        assertTrue(validationResult.getWarnings().isEmpty());

        EvaluationResult evaluationResult0 = rulesEngine.evaluate(EvaluationContext.initial());
        assertEquals(Collections.singleton("fact1"), evaluationResult0.getRequestedFacts());
        assertTrue(evaluationResult0.getGatheredFacts().isEmpty());
        assertFalse(evaluationResult0.isFinished());
        assertNull(evaluationResult0.getMatchedRule());

        EvaluationResult evaluationResult1 =
                rulesEngine.evaluate(EvaluationContext.fromResult(
                        evaluationResult0,
                        Collections.singletonMap("fact1", true)));
        assertTrue(evaluationResult1.getRequestedFacts().isEmpty());
        assertEquals(Collections.singletonMap("fact1", true), evaluationResult1.getGatheredFacts());
        assertTrue(evaluationResult1.isFinished());
        assertEquals("rule1", evaluationResult1.getMatchedRule().getName());
        assertEquals(Collections.singletonMap("fact1", true), evaluationResult1.getMatchedRule().getCondition());
        assertEquals(Collections.singletonList("action1"), evaluationResult1.getMatchedRule().getActions());

        EvaluationResult evaluationResult2 =
                rulesEngine.evaluate(EvaluationContext.fromResult(
                        evaluationResult0,
                        Collections.singletonMap("fact1", false)));
        assertTrue(evaluationResult2.getRequestedFacts().isEmpty());
        assertEquals(Collections.singletonMap("fact1", false), evaluationResult2.getGatheredFacts());
        assertTrue(evaluationResult2.isFinished());
        assertEquals("rule2", evaluationResult2.getMatchedRule().getName());
        assertEquals(Collections.singletonMap("fact1", false), evaluationResult2.getMatchedRule().getCondition());
        assertEquals(Collections.singletonList("action2"), evaluationResult2.getMatchedRule().getActions());
    }
}
