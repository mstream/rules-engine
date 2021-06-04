package com.mstream.rulesengine.ratpack.demo;


import java.util.function.UnaryOperator;

public class RulesStringKeywordShortener implements UnaryOperator<String> {
    @Override
    public String apply(String s) {
        return s.replaceAll(
                ":[^/]+/(?<keywordShortName>\\S+)",
                ":${keywordShortName}");
    }
}
