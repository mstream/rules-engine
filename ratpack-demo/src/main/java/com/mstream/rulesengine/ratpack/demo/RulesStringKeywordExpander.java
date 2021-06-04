package com.mstream.rulesengine.ratpack.demo;


import java.util.function.UnaryOperator;

public class RulesStringKeywordExpander implements UnaryOperator<String> {
    @Override
    public String apply(String s) {
        return s.replaceAll(
                ":(?<keywordName>\\S+)",
                ":com.mstream.rules-engine.core.rule/${keywordName}");
    }
}
