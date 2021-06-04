package com.mstream.rulesengine.core;

import java.util.Map;
import java.util.Set;

public interface ValidationResult {

    Set<String> getFacts();

    Set<String> getActions();

    Map<String, String> getErrors();

    Map<String, String> getWarnings();
}
