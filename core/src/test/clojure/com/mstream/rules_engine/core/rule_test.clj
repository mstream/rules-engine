(ns com.mstream.rules-engine.core.rule-test
  (:require [clojure.test :as test]
            [com.mstream.rules-engine.core.rule :as under-test]))


(def rules-actions-scenarios
  [{::description     "no rules"
    ::rules           []
    ::expected-result #{}}

   {::description     "one rule with one action"
    ::rules           [{::under-test/name       "rule1"
                        ::under-test/conditions []
                        ::under-test/actions    ["action1"]}]
    ::expected-result #{"action1"}}

   {::description     "one rule with two actions"
    ::rules           [{::under-test/name       "rule1"
                        ::under-test/conditions []
                        ::under-test/actions    ["action1"
                                                 "action2"]}]
    ::expected-result #{"action1"
                        "action2"}}

   {::description     "two rules with one action each"
    ::rules           [{::under-test/name       "rule1"
                        ::under-test/conditions []
                        ::under-test/actions    ["action1"]}
                       {::under-test/name       "rule2"
                        ::under-test/conditions []
                        ::under-test/actions    ["action2"]}]
    ::expected-result #{"action1"
                        "action2"}}

   {::description     "two rules with two actions each"
    ::rules           [{::under-test/name       "rule1"
                        ::under-test/conditions []
                        ::under-test/actions    ["action1"
                                                 "action2"]}
                       {::under-test/name       "rule2"
                        ::under-test/conditions []
                        ::under-test/actions    ["action3"
                                                 "action4"]}]
    ::expected-result #{"action1"
                        "action2"
                        "action3"
                        "action4"}}])


(def rules-fact-names-scenarios
  [{::description     "no rules"
    ::rules           []
    ::expected-result #{}}

   {::description     "one rule with one condition"
    ::rules           [{::under-test/name       "rule1"
                        ::under-test/conditions [{"fact1" false
                                                  "fact2" false}]
                        ::under-test/actions    []}]
    ::expected-result #{"fact1"
                        "fact2"}}

   {::description     "one rule with two conditions"
    ::rules           [{::under-test/name       "rule1"
                        ::under-test/conditions [{"fact1" false
                                                  "fact2" false}
                                                 {"fact3" false
                                                  "fact4" false}]
                        ::under-test/actions    []}]
    ::expected-result #{"fact1"
                        "fact2"
                        "fact3"
                        "fact4"}}

   {::description     "two rules with one condition each"
    ::rules           [{::under-test/name       "rule1"
                        ::under-test/conditions [{"fact1" false
                                                  "fact2" false}]
                        ::under-test/actions    []}
                       {::under-test/name       "rule2"
                        ::under-test/conditions [{"fact3" false
                                                  "fact4" false}]
                        ::under-test/actions    []}]
    ::expected-result #{"fact1"
                        "fact2"
                        "fact3"
                        "fact4"}}

   {::description     "two rules with two conditions each"
    ::rules           [{::under-test/name       "rule1"
                        ::under-test/conditions [{"fact1" false
                                                  "fact2" false}
                                                 {"fact3" false
                                                  "fact4" false}]
                        ::under-test/actions    []}
                       {::under-test/name       "rule2"
                        ::under-test/conditions [{"fact5" false
                                                  "fact6" false}
                                                 {"fact7" false
                                                  "fact8" false}]
                        ::under-test/actions    []}]
    ::expected-result #{"fact1"
                        "fact2"
                        "fact3"
                        "fact4"
                        "fact5"
                        "fact6"
                        "fact7"
                        "fact8"}}])


(test/deftest rules-actions
  (doseq [{:keys [::description
                  ::rules
                  ::expected-result]}
          rules-actions-scenarios]
    (test/is (= expected-result
                (under-test/rules-actions rules))
             (str "rules-actions: "
                  description))))


(test/deftest rules-fact-names
  (doseq [{:keys [::description
                  ::rules
                  ::expected-result]}
          rules-fact-names-scenarios]
    (test/is (= expected-result
                (under-test/rules-fact-names rules))
             (str "rules-fact-names: "
                  description))))


(test/deftest ednStr->rules
  (test/is (= [{::under-test/name       "rule1"
                ::under-test/conditions [{"fact1" true}]
                ::under-test/actions    ["action1"]}]
              (under-test/ednStr->rules "[{:name \"rule1\" :conditions [{\"fact1\" true}] :actions [\"action1\"]}]"))))


(test/deftest jsonStr->rules
  (test/is (= [{::under-test/name       "rule1"
                ::under-test/conditions [{"fact1" true}]
                ::under-test/actions    ["action1"]}]
              (under-test/jsonStr->rules "[{\"name\": \"rule1\", \"conditions\": [{\"fact1\": true}] \"actions\": [\"action1\"]}]"))))
