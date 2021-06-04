(ns com.mstream.rules-engine.core.evaluation-test
  (:require [clojure.test :as test]
            [com.mstream.rules-engine.core.evaluation :as under-test]
            [com.mstream.rules-engine.core.rule :as rule]))


(def one-fact-rules
  [{::rule/name       "rule1"
    ::rule/conditions [{"fact1" true}]
    ::rule/actions    ["action1"]}

   {::rule/name       "rule2"
    ::rule/conditions [{"fact1" false}]
    ::rule/actions    ["action2"]}])


(def full-coverage-rules
  [{::rule/name       "rule1"
    ::rule/conditions [{"fact1" true}
                       {"fact1" false
                        "fact2" true}]
    ::rule/actions    ["action1"]}

   {::rule/name       "rule2"
    ::rule/conditions [{"fact1" false
                        "fact2" false}]
    ::rule/actions    ["action2"]}])


(def missing-coverage-rules
  [{::rule/name       "rule1"
    ::rule/conditions [{"fact1" true}]
    ::rule/actions    ["action1"]}

   {::rule/name       "rule2"
    ::rule/conditions [{"fact1" false
                        "fact2" true}]
    ::rule/actions    ["action2"]}])


(def one-fact-rules-scenarios
  [{::description     "first step"
    ::rules           one-fact-rules
    ::context         {::under-test/gathered-facts  {}}
    ::expected-result {::under-test/finished?       false
                       ::under-test/gathered-facts  {}
                       ::under-test/matched-rule    nil
                       ::under-test/requested-facts #{"fact1"}}}

   {::description     "second step - immediate match after providing new facts"
    ::rules           one-fact-rules
    ::context         {::under-test/gathered-facts {"fact1" true}}
    ::expected-result {::under-test/finished?       true
                       ::under-test/gathered-facts  {"fact1" true}
                       ::under-test/matched-rule    {::rule/actions   ["action1"]
                                                     ::rule/condition {"fact1" true}
                                                     ::rule/name      "rule1"}
                       ::under-test/requested-facts #{}}}

   {::description     "second step - match of the second rule after providing new facts"
    ::rules           one-fact-rules
    ::context         {::under-test/gathered-facts {"fact1" false}}
    ::expected-result {::under-test/finished?       true
                       ::under-test/gathered-facts  {"fact1" false}
                       ::under-test/matched-rule    {::rule/actions   ["action2"]
                                                     ::rule/condition {"fact1" false}
                                                     ::rule/name      "rule2"}
                       ::under-test/requested-facts #{}}}])


(def full-coverage-scenarios
  [{::description     "first step"
    ::rules           full-coverage-rules
    ::context         {::under-test/gathered-facts  {}}
    ::expected-result { ::under-test/finished?       false
                       ::under-test/gathered-facts  {}
                       ::under-test/matched-rule    nil
                       ::under-test/requested-facts #{"fact1"}}}

   {::description     "second step - immediate match after providing new facts"
    ::rules           full-coverage-rules
    ::context         {::under-test/gathered-facts {"fact1" true}}
    ::expected-result {::under-test/finished?       true
                       ::under-test/gathered-facts  {"fact1" true}
                       ::under-test/matched-rule    {::rule/actions   ["action1"]
                                                     ::rule/condition {"fact1" true}
                                                     ::rule/name      "rule1"}
                       ::under-test/requested-facts #{}}}

   {::description     "second step - no match and new facts requested"
    ::rules           full-coverage-rules
    ::context         {::under-test/gathered-facts {"fact1" false}}
    ::expected-result { ::under-test/finished?       false
                       ::under-test/gathered-facts  {"fact1" false}
                       ::under-test/matched-rule    nil
                       ::under-test/requested-facts #{"fact2"}}}

   {::description     "third step - immediate match after providing new facts"
    ::rules           full-coverage-rules
    ::context         {::under-test/gathered-facts {"fact1" false
                                                    "fact2" true}}
    ::expected-result {::under-test/finished?       true
                       ::under-test/gathered-facts  {"fact1" false
                                                     "fact2" true}
                       ::under-test/matched-rule    {::rule/actions   ["action1"]
                                                     ::rule/condition {"fact1" false
                                                                       "fact2" true}
                                                     ::rule/name      "rule1"}
                       ::under-test/requested-facts #{}}}

   {::description     "third step - no match and no new facts needed"
    ::rules           full-coverage-rules
    ::context         {::under-test/gathered-facts {"fact1" false
                                                    "fact2" false}}
    ::expected-result {::under-test/finished?       true
                       ::under-test/gathered-facts  {"fact1" false
                                                     "fact2" false}
                       ::under-test/matched-rule    {::rule/actions   ["action2"]
                                                     ::rule/condition {"fact1" false
                                                                       "fact2" false}
                                                     ::rule/name      "rule2"}
                       ::under-test/requested-facts #{}}}])


(def missing-coverage-scenarios
  [{::description     "first step"
    ::rules           missing-coverage-rules
    ::context         {::under-test/gathered-facts  {}}
    ::expected-result {::under-test/finished?       false
                       ::under-test/gathered-facts  {}
                       ::under-test/matched-rule    nil
                       ::under-test/requested-facts #{"fact1"}}}

   {::description     "second step - immediate match after providing new facts"
    ::rules           missing-coverage-rules
    ::context         {::under-test/gathered-facts {"fact1" true}}
    ::expected-result {::under-test/finished?       true
                       ::under-test/gathered-facts  {"fact1" true}
                       ::under-test/matched-rule    {::rule/actions   ["action1"]
                                                     ::rule/condition {"fact1" true}
                                                     ::rule/name      "rule1"}
                       ::under-test/requested-facts #{}}}

   {::description     "second step - no match and new facts requested"
    ::rules           missing-coverage-rules
    ::context         {::under-test/gathered-facts {"fact1" false}}
    ::expected-result {::under-test/finished?       false
                       ::under-test/gathered-facts  {"fact1" false}
                       ::under-test/matched-rule    nil
                       ::under-test/requested-facts #{"fact2"}}}

   {::description     "third step - immediate match after providing new facts"
    ::rules           missing-coverage-rules
    ::context         {::under-test/gathered-facts {"fact1" false
                                                    "fact2" true}}
    ::expected-result {::under-test/finished?       true
                       ::under-test/gathered-facts  {"fact1" false
                                                     "fact2" true}
                       ::under-test/matched-rule    {::rule/actions   ["action2"]
                                                     ::rule/condition {"fact1" false
                                                                       "fact2" true}
                                                     ::rule/name      "rule2"}
                       ::under-test/requested-facts #{}}}
   {::description     "third step - no match and no new facts needed"
    ::rules           missing-coverage-rules
    ::context         {::under-test/gathered-facts {"fact1" false
                                                    "fact2" false}}
    ::expected-result { ::under-test/finished?       true
                       ::under-test/gathered-facts  {"fact1" false
                                                     "fact2" false}
                       ::under-test/matched-rule    nil
                       ::under-test/requested-facts #{}}}])


(test/deftest one-fact-rules-evaluate
  (doseq [{:keys [::context
                  ::description
                  ::expected-result
                  ::rules]} one-fact-rules-scenarios]
    (test/is (= expected-result
                (under-test/evaluate rules
                                     context))
             (str "one-fact-rules-evaluate: "
                  description))))


(test/deftest full-coverage-evaluate
  (doseq [{:keys [::context
                  ::description
                  ::expected-result
                  ::rules]} full-coverage-scenarios]
    (test/is (= expected-result
                (under-test/evaluate rules
                                     context))
             (str "full-coverage-evaluate: "
                  description))))


(test/deftest missing-coverage-evaluate
  (doseq [{:keys [::context
                  ::description
                  ::expected-result
                  ::rules]}
          missing-coverage-scenarios]
    (test/is (= expected-result
                (under-test/evaluate rules
                                     context))
             (str "missing-coverage-evaluate: "
                  description))))


(test/deftest failed-evaluation
  (test/is (thrown-with-msg? AssertionError
                             #"invalid rules schema"
                             (under-test/evaluate [{}]
                                                  {}))
           "invalid schema"))
