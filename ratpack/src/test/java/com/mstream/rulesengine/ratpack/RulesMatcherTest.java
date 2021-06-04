package com.mstream.rulesengine.ratpack;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.core.EvaluationResult;
import com.mstream.rulesengine.core.Rule;
import com.mstream.rulesengine.core.RulesEngine;
import com.mstream.rulesengine.ratpack.config.Data;
import com.mstream.rulesengine.ratpack.config.FactProviderCreator;
import org.junit.Test;
import ratpack.exec.ExecResult;
import ratpack.exec.Promise;
import ratpack.func.Pair;
import ratpack.handling.Context;
import ratpack.registry.Registry;
import ratpack.test.exec.ExecHarness;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RulesMatcherTest {

    private final RulesMatcher underTest = new RulesMatcher();

    @Test
    public void name() throws Exception {

        String fact1 = "fact1";
        String fact2 = "fact2";

        Rule matchedRule = mock(Rule.class);

        Context context = mock(Context.class);

        Data data1 = Data.create(context).withObjects(Registry.single("string"));

        Data data2 = Data.create(context).withObjects(Registry.single(1));

        RulesEngine rulesEngine = mock(RulesEngine.class);

        FactProviderCreator factProviderCreator1 = mock(FactProviderCreator.class);

        FactProviderCreator factProviderCreator2 = mock(FactProviderCreator.class);

        when(factProviderCreator1.getFactName()).thenReturn(fact1);
        when(factProviderCreator1.apply(any())).thenReturn(Promise.value(Pair.of(true, data1)));

        when(factProviderCreator2.getFactName()).thenReturn(fact2);
        when(factProviderCreator2.apply(any())).thenReturn(Promise.value(Pair.of(true, data2)));

        Map<String, FactProviderCreator> factProviderCreators = ImmutableMap.of(
                fact1, factProviderCreator1,
                fact2, factProviderCreator2
        );

        EvaluationResult evaluationResult1 = mock(EvaluationResult.class);
        EvaluationResult evaluationResult2 = mock(EvaluationResult.class);
        EvaluationResult evaluationResult3 = mock(EvaluationResult.class);

        when(rulesEngine.evaluate(any()))
                .thenReturn(evaluationResult1)
                .thenReturn(evaluationResult2)
                .thenReturn(evaluationResult3);

        when(evaluationResult1.isFinished()).thenReturn(false);
        when(evaluationResult1.getRequestedFacts()).thenReturn(ImmutableSet.of(fact1));

        when(evaluationResult2.isFinished()).thenReturn(false);
        when(evaluationResult2.getRequestedFacts()).thenReturn(ImmutableSet.of(fact2));

        when(evaluationResult3.isFinished()).thenReturn(true);
        when(evaluationResult3.getMatchedRule()).thenReturn(matchedRule);

        ExecResult<Pair<Rule, Data>> result = ExecHarness.yieldSingle(exec -> underTest.matchRule(
                rulesEngine,
                factProviderCreators,
                context));

        assertTrue(result.isSuccess());
        assertEquals(matchedRule, result.getValue().left());
        assertEquals("string", result.getValue().right().getObjects().get(String.class));
        assertEquals(Integer.valueOf(1), result.getValue().right().getObjects().get(Integer.class));
    }
}
