(ns com.mstream.rules-engine.core.fact-test
  (:require [clojure.test :as test]
            [com.mstream.rules-engine.core.fact :as under-test]))


(def fact-combinations-scenarios
  [{::description     "no fact names"
    ::fact-names      #{}
    ::expected-result #{}}

   {::description     "one fact name"
    ::fact-names      #{"fact1"}
    ::expected-result #{{"fact1" false}
                        {"fact1" true}}}

   {::description     "two fact names"
    ::fact-names      #{"fact1"
                        "fact2"}
    ::expected-result #{{"fact1" false
                         "fact2" false}
                        {"fact1" false
                         "fact2" true}
                        {"fact1" true
                         "fact2" false}
                        {"fact1" true
                         "fact2" true}}}])


(def expand-facts-scenarios
  [{::description     "test1"
    ::facts           {}
    ::fact-names      #{}
    ::expected-result #{}}

   {::description     "test2"
    ::facts           {"fact1" false}
    ::fact-names      #{"fact1"}
    ::expected-result #{{"fact1" false}}}

   {::description     "test3"
    ::facts           {"fact1" false}
    ::fact-names      #{"fact1"
                        "fact2"}
    ::expected-result #{{"fact1" false
                         "fact2" false}
                        {"fact1" false
                         "fact2" true}}}])


(test/deftest fact-combinations
  (doseq [{:keys [::description
                  ::fact-names
                  ::expected-result]}
          fact-combinations-scenarios]
    (test/is (= expected-result
                (under-test/fact-combinations fact-names))
             (str "fact-combinations: "
                  description))))


(test/deftest expand-facts
  (doseq [{:keys [::description
                  ::facts
                  ::fact-names
                  ::expected-result]}
          expand-facts-scenarios]
    (test/is (= expected-result
                (under-test/expand-facts facts
                                         fact-names))
             (str "expand-facts: "
                  description))))
