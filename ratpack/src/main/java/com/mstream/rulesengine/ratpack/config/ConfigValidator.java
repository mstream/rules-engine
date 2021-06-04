package com.mstream.rulesengine.ratpack.config;

import com.mstream.rulesengine.core.ValidationOptions;
import com.mstream.rulesengine.core.ValidationResult;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigValidator {

    public void validate(
            Config config,
            ValidationOptions options) {

        ValidationResult validationResult = config.getRulesEngine().validate(options);

        if (!validationResult.getErrors().isEmpty()) {
            throw new IllegalArgumentException("invalid rules: " + validationResult.getErrors());
        }

        if (!validationResult.getWarnings().isEmpty()) {
            throw new IllegalArgumentException("ugly rules: " + validationResult.getWarnings());
        }

        Set<String> actions = validationResult.getActions();
        Set<String> facts = validationResult.getFacts();

        validateFactProviders(facts, config.getFactProviderCreators());
        validateActionHandlers(actions, config.getActionPreHandlerCreators(), config.getActionHandlerCreators());
    }

    private static void validateActionHandlers(
            Set<String> actions,
            Map<String, ActionPreHandlerCreator> actionPreHandlerCreators,
            Map<String, ActionHandlerCreator> actionHandlerCreators) {
        Set<String> missingActionHandlers = actions.stream().filter(action ->
                !actionHandlerCreators.keySet().contains(action)).collect(Collectors.toSet());

        if (!missingActionHandlers.isEmpty()) {
            throw new IllegalArgumentException("missing handlers for some actions: " + missingActionHandlers);
        }

        Set<Class<?>> requiredObjects = actionHandlerCreators
                .values()
                .stream()
                .flatMap(actionHandlerCreator -> actionHandlerCreator.getRequiredObjects().stream())
                .collect(Collectors.toSet());

        Set<Class<?>> availableObjects = actionPreHandlerCreators
                .values()
                .stream()
                .flatMap(actionHandlerCreator -> actionHandlerCreator.getProducedObject().stream())
                .collect(Collectors.toSet());

        Set<Class<?>> missingObjects = requiredObjects
                .stream()
                .filter(object -> !availableObjects.contains(object))
                .collect(Collectors.toSet());

        if (!missingObjects.isEmpty()) {
            throw new IllegalArgumentException("Missing pre-handler(s) providing objects: " + missingObjects);
        }
    }

    private static void validateFactProviders(
            Set<String> facts,
            Map<String, FactProviderCreator> factProviders) {
        Set<String> missingFactProviders = facts.stream().filter(factName ->
                !factProviders.keySet().contains(factName)).collect(Collectors.toSet());

        if (!missingFactProviders.isEmpty()) {
            throw new IllegalArgumentException("missing providers for some facts: " + missingFactProviders);
        }
    }
}
