(ns com.mstream.rules-engine.java.rules-engine
  (:require [clojure.edn :as edn]
            [com.mstream.rules-engine.core.evaluation :as evaluation]
            [com.mstream.rules-engine.core.rule :as rule]
            [com.mstream.rules-engine.core.validation :as validation]
            [com.mstream.rules-engine.java.evaluation-result]
            [com.mstream.rules-engine.java.rule]
            [com.mstream.rules-engine.java.validation-result])
  (:import [com.mstream.rules_engine.java.validation_result ValidationResult]
           [com.mstream.rules_engine.java.evaluation_result EvaluationResult]
           [com.mstream.rules_engine.java.rule Rule]))


(defrecord RulesEngine
  [rules]
  com.mstream.rulesengine.core.RulesEngine
  (validate [_
             options]
    (let [{:keys [::validation/errors
                  ::validation/warnings
                  ::validation/info]}
          (validation/validate-rules rules
                                     {::validation/ignored-warnings
                                      (into #{}
                                            (map (fn [keyword-name]
                                                   (keyword "com.mstream.rules-engine.core.validation"
                                                            keyword-name))
                                                 (.getExcludedWarnings options)))})]
      (ValidationResult. errors
                         warnings
                         (::validation/actions info)
                         (::validation/facts info))))
  (evaluate [_ evaluation-context]
    (let [{:keys [::evaluation/finished?
                  ::evaluation/gathered-facts
                  ::evaluation/matched-rule
                  ::evaluation/requested-facts]}
          (evaluation/evaluate rules
                               {::evaluation/gathered-facts (into {}
                                                                  (.getGatheredFacts evaluation-context))})]
      (EvaluationResult. finished?
                         gathered-facts
                         (when (some? matched-rule)
                           (Rule. (::rule/name matched-rule)
                                  (::rule/condition matched-rule)
                                  (::rule/actions matched-rule)))
                         requested-facts)))

  )
