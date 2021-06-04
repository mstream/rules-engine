package com.mstream.rulesengine.ratpack.demo;

import org.junit.Test;

import static org.junit.Assert.*;

public class RulesStringKeywordShortenerTest {
    private static final String INPUT = "[" +
            "{" +
            ":com.mstream.rules-engine.core.rule/name \"HOUSEHOLD_IS_BLOCKED\" " +
            ":com.mstream.rules-engine.core.rule/conditions [{\"IS_HOUSEHOLD_BLOCKED\" true}] " +
            ":com.mstream.rules-engine.core.rule/actions [\"RETURN_STREAMING_FORBIDDEN_RESPONSE\"] " +
            "}" +
            " " +
            "{" +
            ":com.mstream.rules-engine.core.rule/name \"HOUSEHOLD_IS_NOT_BLOCKED\" " +
            ":com.mstream.rules-engine.core.rule/conditions [{\"IS_HOUSEHOLD_BLOCKED\" false}] " +
            ":com.mstream.rules-engine.core.rule/actions [\"RETURN_STREAMING_ALLOWED_RESPONSE\"] " +
            "}" +
            "]";

    private static final String EXPECTED_OUTPUT = "[" +
            "{" +
            ":name \"HOUSEHOLD_IS_BLOCKED\" " +
            ":conditions [{\"IS_HOUSEHOLD_BLOCKED\" true}] " +
            ":actions [\"RETURN_STREAMING_FORBIDDEN_RESPONSE\"] " +
            "}" +
            " " +
            "{" +
            ":name \"HOUSEHOLD_IS_NOT_BLOCKED\" " +
            ":conditions [{\"IS_HOUSEHOLD_BLOCKED\" false}] " +
            ":actions [\"RETURN_STREAMING_ALLOWED_RESPONSE\"] " +
            "}" +
            "]";

    @Test
    public void test() {
        assertEquals(EXPECTED_OUTPUT, new RulesStringKeywordShortener().apply(INPUT));
    }
}