package com.mstream.rulesengine.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Rule {

    String getName();

    List<String> getActions();

    Map<String, Boolean> getCondition();
}
