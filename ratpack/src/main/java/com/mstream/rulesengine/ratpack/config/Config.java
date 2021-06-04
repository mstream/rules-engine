package com.mstream.rulesengine.ratpack.config;

import com.mstream.rulesengine.core.RulesEngine;

import java.util.Map;


public class Config {
    private final RulesEngine rulesEngine;
    private final Map<String, FactProviderCreator> factProviderCreators;
    private final Map<String, ActionPreHandlerCreator> actionPreHandlerCreators;
    private final Map<String, ActionHandlerCreator> actionHandlerCreators;

    public Config(
            RulesEngine rulesEngine,
            Map<String, FactProviderCreator> factProviderCreators,
            Map<String, ActionPreHandlerCreator> actionPreHandlerCreators,
            Map<String, ActionHandlerCreator> actionHandlerCreators) {
        this.rulesEngine = rulesEngine;
        this.factProviderCreators = factProviderCreators;
        this.actionPreHandlerCreators = actionPreHandlerCreators;
        this.actionHandlerCreators = actionHandlerCreators;
    }

    public RulesEngine getRulesEngine() {
        return rulesEngine;
    }

    public Map<String, FactProviderCreator> getFactProviderCreators() {
        return factProviderCreators;
    }

    public Map<String, ActionPreHandlerCreator> getActionPreHandlerCreators() {
        return actionPreHandlerCreators;
    }

    public Map<String, ActionHandlerCreator> getActionHandlerCreators() {
        return actionHandlerCreators;
    }
}
