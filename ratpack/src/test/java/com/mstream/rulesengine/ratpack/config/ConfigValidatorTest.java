package com.mstream.rulesengine.ratpack.config;

import com.google.common.collect.ImmutableMap;
import com.mstream.rulesengine.core.RulesEngine;
import com.mstream.rulesengine.core.ValidationOptions;
import com.mstream.rulesengine.core.ValidationResult;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigValidatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnInvalidRules() {
        RulesEngine rulesEngine = mock(RulesEngine.class);
        ValidationResult validationResult = mock(ValidationResult.class);
        when(rulesEngine.validate(any())).thenReturn(validationResult);
        when(validationResult.getErrors()).thenReturn(
                Collections.singletonMap("errorName", "errorDescription"));

        Config config = new Config(
                rulesEngine,
                ImmutableMap.of(),
                ImmutableMap.of(),
                ImmutableMap.of());

        ConfigValidator underTest = new ConfigValidator();

        underTest.validate(config, new ValidationOptions());
    }


    @Test(expected = IllegalArgumentException.class)
    public void throwsOnUglyRules() {
        RulesEngine rulesEngine = mock(RulesEngine.class);
        ValidationResult validationResult = mock(ValidationResult.class);
        when(rulesEngine.validate(any())).thenReturn(validationResult);
        when(validationResult.getWarnings()).thenReturn(
                Collections.singletonMap("warningName", "warningDescription"));

        Config config = new Config(
                rulesEngine,
                ImmutableMap.of(),
                ImmutableMap.of(),
                ImmutableMap.of());

        ConfigValidator underTest = new ConfigValidator();

        underTest.validate(config, new ValidationOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnMissingFactProviders() {
        RulesEngine rulesEngine = mock(RulesEngine.class);
        ValidationResult validationResult = mock(ValidationResult.class);
        when(rulesEngine.validate(any())).thenReturn(validationResult);
        when(validationResult.getFacts()).thenReturn(
                Collections.singleton("fact1"));

        Config config = new Config(
                rulesEngine,
                ImmutableMap.of(),
                ImmutableMap.of(),
                ImmutableMap.of());

        ConfigValidator underTest = new ConfigValidator();

        underTest.validate(config, new ValidationOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnMissingActionHandlers() {
        RulesEngine rulesEngine = mock(RulesEngine.class);
        ValidationResult validationResult = mock(ValidationResult.class);
        when(rulesEngine.validate(any())).thenReturn(validationResult);
        when(validationResult.getActions()).thenReturn(
                Collections.singleton("action1"));

        Config config = new Config(
                rulesEngine,
                ImmutableMap.of(),
                ImmutableMap.of(),
                ImmutableMap.of());

        ConfigValidator underTest = new ConfigValidator();

        underTest.validate(config, new ValidationOptions());
    }

    @Test
    public void valid() {
        RulesEngine rulesEngine = mock(RulesEngine.class);
        ValidationResult validationResult = mock(ValidationResult.class);
        when(rulesEngine.validate(any())).thenReturn(validationResult);
        when(validationResult.getFacts()).thenReturn(
                Collections.singleton("fact1"));
        when(validationResult.getActions()).thenReturn(
                Collections.singleton("action1"));

        FactProviderCreator factProviderCreator = mock(FactProviderCreator.class);
        ActionHandlerCreator actionHandlerCreator = mock(ActionHandlerCreator.class);

        Config config = new Config(
                rulesEngine,
                ImmutableMap.of("fact1", factProviderCreator),
                ImmutableMap.of(),
                ImmutableMap.of("action1", actionHandlerCreator));

        ConfigValidator underTest = new ConfigValidator();

        underTest.validate(config, new ValidationOptions());
    }
}
