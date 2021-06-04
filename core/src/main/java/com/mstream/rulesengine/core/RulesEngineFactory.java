package com.mstream.rulesengine.core;


import clojure.java.api.Clojure;
import clojure.lang.IFn;


public class RulesEngineFactory {

    static {
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("com.mstream.rules_engine.java.rules_engine"));
        require.invoke(Clojure.read("com.mstream.rules_engine.core.rule"));
    }


    private static RulesEngine create(Object rules) {
        return (RulesEngine) Clojure.var(
                "com.mstream.rules-engine.java.rules-engine",
                "->RulesEngine")
                .invoke(rules);
    }


    public static RulesEngine fromEdn(String rulesEdnString) {
        return create(Clojure.var(
                "com.mstream.rules-engine.core.rule",
                "ednStr->rules")
                .invoke(rulesEdnString));
    }


    public static RulesEngine fromJson(String rulesJsonString) {
        return create(Clojure.var(
                "com.mstream.rules-engine.core.rule",
                "jsonStr->rules")
                .invoke(rulesJsonString));
    }
}
