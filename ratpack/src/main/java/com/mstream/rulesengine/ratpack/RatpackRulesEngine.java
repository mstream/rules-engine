package com.mstream.rulesengine.ratpack;

import com.mstream.rulesengine.core.Rule;
import com.mstream.rulesengine.core.ValidationOptions;
import com.mstream.rulesengine.ratpack.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.exec.util.ParallelBatch;
import ratpack.func.Pair;
import ratpack.handling.Context;

import java.util.Set;
import java.util.stream.Collectors;


public class RatpackRulesEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatpackRulesEngine.class);

    public Promise<Void> evaluate(
            Config config,
            Context context
    ) {
        context
                .get(ConfigValidator.class)
                .validate(config, context.get(ValidationOptions.class));

        Promise<Pair<Rule, Data>> ruleAndDataPromise = context
                .get(RulesMatcher.class)
                .matchRule(
                        config.getRulesEngine(),
                        config.getFactProviderCreators(),
                        context
                );

        return ruleAndDataPromise
                .flatMap(ruleAndData -> {
                    Set<ActionHandlerCreator> actionHandlerCreators = ruleAndData
                            .left()
                            .getActions()
                            .stream()
                            .map(action -> config.getActionHandlerCreators().get(action))
                            .collect(Collectors.toSet());

                    Set<Class<?>> requiredObjects = actionHandlerCreators
                            .stream()
                            .flatMap(actionHandlerCreator -> actionHandlerCreator.getRequiredObjects().stream())
                            .collect(Collectors.toSet());

                    Set<ActionPreHandlerCreator> actionPreHandlerCreators = config.getActionPreHandlerCreators()
                            .values()
                            .stream()
                            .filter(actionHandlerCreator ->
                                    actionHandlerCreator.getProducedObject().stream().anyMatch(requiredObjects::contains))
                            .collect(Collectors.toSet());

                    return actionPreHandlerCreators.isEmpty() ?
                            Promise.value(
                                    actionHandlerCreators
                                            .stream()
                                            .map(actionHandlerCreator -> actionHandlerCreator.apply(ruleAndData.right()))
                                            .collect(Collectors.toSet())
                            ) : ParallelBatch.of(
                            actionPreHandlerCreators
                                    .stream()
                                    .map(actionPreHandlerCreator -> actionPreHandlerCreator.apply(ruleAndData.right()))
                                    .collect(Collectors.toSet()))
                            .yield()
                            .map(dataList -> {
                                Data newData = dataList.stream().reduce((acc, data) -> acc.withObjects(data.getObjects())).orElseThrow(IllegalStateException::new);
                                return actionHandlerCreators
                                        .stream()
                                        .map(actionHandlerCreator -> actionHandlerCreator.apply(newData))
                                        .collect(Collectors.toSet());
                            });
                }).flatMap(actionPreHandlers -> actionPreHandlers.isEmpty() ?
                        Promise.ofNull() :
                        ParallelBatch.of(actionPreHandlers)
                                .yield()
                                .map(ignore -> null));
    }
}
