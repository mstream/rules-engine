package com.mstream.rulesengine.ratpack;

import com.mstream.rulesengine.core.EvaluationContext;
import com.mstream.rulesengine.core.EvaluationResult;
import com.mstream.rulesengine.core.Rule;
import com.mstream.rulesengine.core.RulesEngine;
import com.mstream.rulesengine.ratpack.config.Data;
import com.mstream.rulesengine.ratpack.config.FactProviderCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.exec.util.ParallelBatch;
import ratpack.func.Pair;
import ratpack.handling.Context;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class RulesMatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesMatcher.class);


    public Promise<Pair<Rule, Data>> matchRule(
            RulesEngine rulesEngine,
            Map<String, FactProviderCreator> factProviderCreators,
            Context context
    ) {
        AtomicReference<Pair<EvaluationResult, Data>> evaluationResultRef =
                new AtomicReference<>(Pair.of(
                        rulesEngine.evaluate(EvaluationContext.initial()),
                        Data.create(context)));

        AtomicReference<Promise<Pair<Rule, Data>>> promiseRef = new AtomicReference<>();

        LOGGER.info("Initial evaluation result: " + evaluationResultRef.get());

        promiseRef.set(
                Promise.ofNull()
                        .flatMap(ignored -> ParallelBatch.of(
                                evaluationResultRef
                                        .get()
                                        .left()
                                        .getRequestedFacts()
                                        .stream()
                                        .map(factName -> Pair.of(factName, factProviderCreators.get(factName)))
                                        .map(factNameAndFactProviderCreator ->
                                                factNameAndFactProviderCreator
                                                        .right()
                                                        .apply(evaluationResultRef.get().right())
                                                        .map(factValueAndData -> Pair.of(factNameAndFactProviderCreator.left(), factValueAndData)))
                                        .collect(Collectors.toList())
                        ).yield())
                        .flatMap(factNamesAndValuesAndData -> {


                            Map<String, Boolean> newFacts = factNamesAndValuesAndData.stream()
                                    .map(factNameAndValueAndData -> Pair.of(factNameAndValueAndData.left(), factNameAndValueAndData.right().left()))
                                    .collect(Collectors.toMap(
                                            Pair::left,
                                            Pair::right
                                    ));

                            Data newData = factNamesAndValuesAndData.stream()
                                    .map(factNameAndValueAndData -> factNameAndValueAndData.right().right())
                                    .reduce(evaluationResultRef.get().right(), (acc, data) -> acc.withObjects(data.getObjects()));

                            EvaluationContext evaluationContext = EvaluationContext.fromResult(
                                    evaluationResultRef.get().left(),
                                    newFacts);

                            LOGGER.info("Evaluation context: " + evaluationContext);

                            EvaluationResult newEvaluationResult = rulesEngine.evaluate(evaluationContext);

                            LOGGER.info("New evaluation result: " + newEvaluationResult);

                            if (newEvaluationResult.isFinished()) {
                                LOGGER.info("Matching rule found: " + newEvaluationResult.getMatchedRule().getName());
                                return Promise.value(Pair.of(newEvaluationResult.getMatchedRule(), newData));
                            } else {
                                evaluationResultRef.set(Pair.of(newEvaluationResult, newData));
                                return promiseRef.get();
                            }
                        }));

        return promiseRef.get();
    }
}
