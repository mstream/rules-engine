package com.mstream.rulesengine.core;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationOptions {

    public enum Exclusions {

        DUPLICATED_ACTIONS("duplicated-actions"),
        DUPLICATED_CONDITIONS("duplicated-conditions"),
        DUPLICATED_RULE_NAMES("duplicated-rule-names"),
        MISSING_CONDITIONS("missing-conditions"),
        RULES_WITHOUT_ACTIONS("rules-without-actions");

        private final String keywordName;

        Exclusions(String keywordName) {
            this.keywordName = keywordName;
        }

        public String getKeywordName() {
            return keywordName;
        }
    }

    private final Set<String> excludedWarnings;

    public ValidationOptions(Exclusions... exclusions) {
        excludedWarnings = Stream.of(exclusions)
                .map(Exclusions::getKeywordName)
                .collect(Collectors.toSet());
    }

    public Set<String> getExcludedWarnings() {
        return excludedWarnings;
    }
}
