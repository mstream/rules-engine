package com.mstream.rulesengine.ratpack;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.core.Rule;
import com.mstream.rulesengine.core.RulesEngine;
import com.mstream.rulesengine.ratpack.config.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ratpack.exec.ExecResult;
import ratpack.exec.Promise;
import ratpack.func.Pair;
import ratpack.handling.Context;
import ratpack.registry.Registry;
import ratpack.test.exec.ExecHarness;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class RatpackRulesEngineTest {


    @Test
    public void evaluatesCorrectly() throws Exception {

        RulesEngine rulesEngine = mock(RulesEngine.class);
        ConfigValidator configValidator = mock(ConfigValidator.class);
        RulesMatcher rulesMatcher = mock(RulesMatcher.class);

        Rule matchedRule = mock(Rule.class);
        when(matchedRule.getActions()).thenReturn(ImmutableList.of("action1", "action2"));

        Context context = mock(Context.class);
        when(context.get(ConfigValidator.class)).thenReturn(configValidator);
        when(context.get(RulesMatcher.class)).thenReturn(rulesMatcher);

        when(rulesMatcher.matchRule(any(), any(), any()))
                .thenReturn(Promise.value(Pair.of(matchedRule, Data.create(context).withObjects(Registry.single(Object.class)))));

        RatpackRulesEngine underTest = new RatpackRulesEngine();

        ActionPreHandlerCreator actionPreHandlerCreator1 = mock(ActionPreHandlerCreator.class);
        ActionPreHandlerCreator actionPreHandlerCreator2 = mock(ActionPreHandlerCreator.class);
        ActionPreHandlerCreator actionPreHandlerCreator3 = mock(ActionPreHandlerCreator.class);
        ActionPreHandlerCreator actionPreHandlerCreator4 = mock(ActionPreHandlerCreator.class);

        when(actionPreHandlerCreator1.getProducedObject())
                .thenReturn(ImmutableSet.of(String.class));
        when(actionPreHandlerCreator1.apply(any()))
                .thenReturn(Promise.value(Data.create(context).withObjects(Registry.single("string"))));

        when(actionPreHandlerCreator2.getProducedObject())
                .thenReturn(ImmutableSet.of(Integer.class));
        when(actionPreHandlerCreator2.apply(any()))
                .thenReturn(Promise.value(Data.create(context).withObjects(Registry.single(1))));

        when(actionPreHandlerCreator3.getProducedObject())
                .thenReturn(ImmutableSet.of(Boolean.class));
        when(actionPreHandlerCreator3.apply(any()))
                .thenReturn(Promise.value(Data.create(context).withObjects(Registry.single(true))));

        when(actionPreHandlerCreator4.getProducedObject())
                .thenReturn(ImmutableSet.of(Double.class));
        when(actionPreHandlerCreator4.apply(any()))
                .thenReturn(Promise.value(Data.create(context).withObjects(Registry.single(2.0))));

        ActionHandlerCreator actionHandlerCreator1 = mock(ActionHandlerCreator.class);
        when(actionHandlerCreator1.getRequiredObjects())
                .thenReturn(ImmutableSet.of(String.class, Integer.class));

        ActionHandlerCreator actionHandlerCreator2 = mock(ActionHandlerCreator.class);
        when(actionHandlerCreator2.getRequiredObjects())
                .thenReturn(ImmutableSet.of(Boolean.class));

        ActionHandlerCreator actionHandlerCreator3 = mock(ActionHandlerCreator.class);
        when(actionHandlerCreator3.getRequiredObjects())
                .thenReturn(ImmutableSet.of(Double.class));


        Promise<Void> testPromise = underTest.evaluate(
                new Config(
                        rulesEngine,
                        Collections.emptyMap(),
                        ImmutableMap.of(
                                "actionPreHandler1", actionPreHandlerCreator1,
                                "actionPreHandler2", actionPreHandlerCreator2,
                                "actionPreHandler3", actionPreHandlerCreator3,
                                "actionPreHandler4", actionPreHandlerCreator4),
                        ImmutableMap.of(
                                "action1", actionHandlerCreator1,
                                "action2", actionHandlerCreator2,
                                "action3", actionHandlerCreator3)),
                context
        );

        ExecResult<Void> execResult = ExecHarness.harness().yield(exec -> testPromise);

        assertTrue(execResult.isSuccess());

        Stream.of(actionPreHandlerCreator1, actionPreHandlerCreator2, actionPreHandlerCreator3)
                .forEach(actionPreHandlerCreator -> {
                    ArgumentCaptor<Data> dataArgumentCaptor = ArgumentCaptor.forClass(Data.class);
                    verify(actionPreHandlerCreator).apply(dataArgumentCaptor.capture());
                    assertTrue(dataArgumentCaptor.getValue().getObjects().maybeGet(Object.class).isPresent());
                });

        verify(actionPreHandlerCreator4, never()).apply(any());

        Stream.of(actionHandlerCreator1, actionHandlerCreator2)
                .forEach(actionHandlerCreator -> {
                    ArgumentCaptor<Data> dataArgumentCaptor = ArgumentCaptor.forClass(Data.class);
                    verify(actionHandlerCreator).apply(dataArgumentCaptor.capture());
                    assertTrue(dataArgumentCaptor.getValue().getObjects().maybeGet(String.class).isPresent());
                    assertTrue(dataArgumentCaptor.getValue().getObjects().maybeGet(Integer.class).isPresent());
                    assertTrue(dataArgumentCaptor.getValue().getObjects().maybeGet(Boolean.class).isPresent());
                    assertFalse(dataArgumentCaptor.getValue().getObjects().maybeGet(Double.class).isPresent());
                });

        verify(actionHandlerCreator3, never()).apply(any());
    }
}
