package com.mstream.rulesengine.ratpack.demo;

import com.mstream.rulesengine.core.RulesEngineFactory;
import com.mstream.rulesengine.ratpack.RatpackRulesEngine;
import com.mstream.rulesengine.ratpack.config.*;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class StartStreamHandler implements Handler {

    @Override
    public void handle(Context context) {

        Map<String, FactProviderCreator> factProviderCreators = StreamSupport
                .stream(context.getAll(FactProviderCreator.class).spliterator(), false)
                .collect(Collectors.toMap(
                        FactProviderCreator::getFactName,
                        Function.identity()));

        Map<String, ActionPreHandlerCreator> actionPreHandlerCreators = StreamSupport
                .stream(context.getAll(ActionPreHandlerCreator.class).spliterator(), false)
                .collect(Collectors.toMap(
                        ActionPreHandlerCreator::getPreHandlerName,
                        Function.identity()));

        Map<String, ActionHandlerCreator> actionHandlerCreators = StreamSupport
                .stream(context.getAll(ActionHandlerCreator.class).spliterator(), false)
                .collect(Collectors.toMap(
                        ActionHandlerCreator::getActionName,
                        Function.identity()));

        Config config = new Config(
                RulesEngineFactory.fromEdn(context.get(RulesSupplier.class).get()),
                    factProviderCreators,
                actionPreHandlerCreators,
                actionHandlerCreators);

        context.get(RatpackRulesEngine.class)
                .evaluate(config, context)
                .then(ignored -> {});
    }
}
