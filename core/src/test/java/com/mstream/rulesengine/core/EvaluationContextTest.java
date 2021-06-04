package com.mstream.rulesengine.core;


import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class EvaluationContextTest {

    @Test
    public void initialContextHasdEmptyGatheredFacts() {
        EvaluationContext underTest = EvaluationContext.initial();
        assertEquals(Collections.emptyMap(), underTest.getGatheredFacts());
    }


    @Test(expected = IllegalArgumentException.class)
    public void throwWhenCreatedFromResultWhichFinishedTheEvaluation() {
        EvaluationResult evaluationResult = mock(EvaluationResult.class);
        when(evaluationResult.isFinished()).thenReturn(true);
        EvaluationContext.fromResult(
                evaluationResult,
                Collections.emptyMap());
    }


    @Test(expected = IllegalArgumentException.class)
    public void throwWhenCreatedFromResultWhichRequiresMoreFactsThanProvided() {
        EvaluationResult evaluationResult = mock(EvaluationResult.class);
        when(evaluationResult.getRequestedFacts()).thenReturn(Collections.singleton("fact1"));
        EvaluationContext.fromResult(
                evaluationResult,
                Collections.emptyMap());
    }


    @Test(expected = IllegalArgumentException.class)
    public void throwWhenCreatedFromResultWhichContainsFactsBeingProvided() {
        EvaluationResult evaluationResult = mock(EvaluationResult.class);
        when(evaluationResult.getGatheredFacts()).thenReturn(Collections.singletonMap("fact1", false));
        EvaluationContext.fromResult(
                evaluationResult,
                Collections.singletonMap("fact1", true));
    }


    @Test
    public void mergesGatheredFactsWithProvided() {
        EvaluationResult evaluationResult = mock(EvaluationResult.class);
        when(evaluationResult.getGatheredFacts()).thenReturn(Collections.singletonMap("fact1", false));
        when(evaluationResult.getRequestedFacts()).thenReturn(Collections.singleton("fact2"));

        EvaluationContext underTest = EvaluationContext.fromResult(
                evaluationResult,
                Collections.singletonMap("fact2", true));

        assertEquals(2, underTest.getGatheredFacts().size());
        assertEquals(false, underTest.getGatheredFacts().get("fact1"));
        assertEquals(true, underTest.getGatheredFacts().get("fact2"));
    }
}
