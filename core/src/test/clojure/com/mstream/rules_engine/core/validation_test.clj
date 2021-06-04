(ns com.mstream.rules-engine.core.validation-test
  (:require [clojure.test :as test]
            [com.mstream.rules-engine.core.validation :as under-test]
            [com.mstream.rules-engine.core.rule :as rule]))


(def scenarios
  [{::description     "full condition coverage"
    ::rules           [{::rule/name       "rule1"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action1"]}
                       {::rule/name       "rule2"
                        ::rule/conditions [{"fact1" false}]
                        ::rule/actions    ["action2"]}]
    ::options         {::under-test/ignored-warnings #{}}
    ::expected-result {::under-test/errors   {}
                       ::under-test/info     {::under-test/actions #{"action1"
                                                                     "action2"}
                                              ::under-test/facts   #{"fact1"}}
                       ::under-test/warnings {}}}

   {::description     "one of rules without any actions"
    ::rules           [{::rule/name       "rule1"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action1"]}
                       {::rule/name       "rule2"
                        ::rule/conditions [{"fact1" false}]
                        ::rule/actions    []}]
    ::options         {::under-test/ignored-warnings #{}}
    ::expected-result {::under-test/errors   {}
                       ::under-test/info     {::under-test/actions #{"action1"}
                                              ::under-test/facts   #{"fact1"}}
                       ::under-test/warnings {::under-test/rules-without-actions #{"rule2"}}}}

   {::description     "one of rules without any actions - suppressed"
    ::rules           [{::rule/name       "rule1"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action1"]}
                       {::rule/name       "rule2"
                        ::rule/conditions [{"fact1" false}]
                        ::rule/actions    []}]
    ::options         {::under-test/ignored-warnings #{::under-test/rules-without-actions}}
    ::expected-result {::under-test/errors   {}
                       ::under-test/info     {::under-test/actions #{"action1"}
                                              ::under-test/facts   #{"fact1"}}
                       ::under-test/warnings {}}}

   {::description     "duplicated rule names"
    ::rules           [{::rule/name       "rule1"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action1"]}
                       {::rule/name       "rule1"
                        ::rule/conditions [{"fact1" false}]
                        ::rule/actions    ["action2"]}]
    ::options         {::under-test/ignored-warnings #{}}
    ::expected-result {::under-test/errors   {}
                       ::under-test/info     {::under-test/actions #{"action1"
                                                                     "action2"}
                                              ::under-test/facts   #{"fact1"}}
                       ::under-test/warnings {::under-test/duplicated-rule-names #{"rule1"}}}}

   {::description     "duplicated rule names - suppressed"
    ::rules           [{::rule/name       "rule1"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action1"]}
                       {::rule/name       "rule1"
                        ::rule/conditions [{"fact1" false}]
                        ::rule/actions    ["action2"]}]
    ::options         {::under-test/ignored-warnings #{::under-test/duplicated-rule-names}}
    ::expected-result {::under-test/errors   {}
                       ::under-test/info     {::under-test/actions #{"action1"
                                                                     "action2"}
                                              ::under-test/facts   #{"fact1"}}
                       ::under-test/warnings {}}}

   {::description     "lack of coverage"
    ::rules           [{::rule/name       "rule1"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action1"]}]
    ::options         {::under-test/ignored-warnings #{}}
    ::expected-result {::under-test/errors   {}
                       ::under-test/info     {::under-test/actions #{"action1"}
                                              ::under-test/facts   #{"fact1"}}
                       ::under-test/warnings {::under-test/missing-conditions #{{"fact1" false}}}}}

   {::description     "lack of coverage - suppressed"
    ::rules           [{::rule/name       "rule1"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action1"]}]
    ::options         {::under-test/ignored-warnings #{::under-test/missing-conditions}}
    ::expected-result {::under-test/errors   {}
                       ::under-test/info     {::under-test/actions #{"action1"}
                                              ::under-test/facts   #{"fact1"}}
                       ::under-test/warnings {}}}

   {::description     "duplicated conditions"
    ::rules           [{::rule/name       "rule1"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action1"]}
                       {::rule/name       "rule2"
                        ::rule/conditions [{"fact1" false}]
                        ::rule/actions    ["action2"]}
                       {::rule/name       "rule3"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action3"]}]
    ::options         {::under-test/ignored-warnings #{}}
    ::expected-result {::under-test/errors   {}
                       ::under-test/info     {::under-test/actions #{"action1"
                                                                     "action2"
                                                                     "action3"}
                                              ::under-test/facts   #{"fact1"}}
                       ::under-test/warnings {::under-test/duplicated-conditions {{"fact1" true} ["rule1"
                                                                                                  "rule3"]}}}}

   {::description     "duplicated conditions - suppressed"
    ::rules           [{::rule/name       "rule1"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action1"]}
                       {::rule/name       "rule2"
                        ::rule/conditions [{"fact1" false}]
                        ::rule/actions    ["action2"]}
                       {::rule/name       "rule3"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action3"]}]
    ::options         {::under-test/ignored-warnings #{::under-test/duplicated-conditions}}
    ::expected-result {::under-test/errors   {}
                       ::under-test/info     {::under-test/actions #{"action1"
                                                                     "action2"
                                                                     "action3"}
                                              ::under-test/facts   #{"fact1"}}
                       ::under-test/warnings {}}}

   {::description     "duplicated actions"
    ::rules           [{::rule/name       "rule1"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action1"]}
                       {::rule/name       "rule2"
                        ::rule/conditions [{"fact1" false}]
                        ::rule/actions    ["action1"]}]
    ::options         {::under-test/ignored-warnings #{}}
    ::expected-result {::under-test/errors   {}
                       ::under-test/info     {::under-test/actions #{"action1"}
                                              ::under-test/facts   #{"fact1"}}
                       ::under-test/warnings {::under-test/duplicated-actions {["action1"] ["rule1"
                                                                                            "rule2"]}}}}

   {::description     "duplicated actions - suppressed"
    ::rules           [{::rule/name       "rule1"
                        ::rule/conditions [{"fact1" true}]
                        ::rule/actions    ["action1"]}
                       {::rule/name       "rule2"
                        ::rule/conditions [{"fact1" false}]
                        ::rule/actions    ["action1"]}]
    ::options         {::under-test/ignored-warnings #{::under-test/duplicated-actions}}
    ::expected-result {::under-test/errors   {}
                       ::under-test/info     {::under-test/actions #{"action1"}
                                              ::under-test/facts   #{"fact1"}}
                       ::under-test/warnings {}}}])


(test/deftest successful-validations
  (doseq [{:keys [::description
                  ::expected-result
                  ::options
                  ::rules]}
          scenarios]
    (test/is (= expected-result
                (under-test/validate-rules rules
                                           options))
             description)))


(test/deftest failed-validations
  (let [{:keys [::under-test/errors]}
        (under-test/validate-rules [{}] {::under-test/ignored-warnings #{}})]
    (test/is (contains? errors
                        ::under-test/invalid-schema)
             "invalid schema")))
