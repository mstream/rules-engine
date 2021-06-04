package com.mstream.rulesengine.core;


import java.util.Map;
import java.util.Set;


public interface EvaluationResult {

    boolean isFinished();

    Rule getMatchedRule();

    Map<String, Boolean> getGatheredFacts();

    Set<String> getRequestedFacts();
}
