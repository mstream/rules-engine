(ns com.mstream.rules-engine.core.evaluation
  (:require [clojure.set :as set]
            [clojure.spec.alpha :as spec]
            [com.mstream.rules-engine.core.fact :as fact]
            [com.mstream.rules-engine.core.rule :as rule]))


(spec/def ::gathered-facts
  ::fact/facts)


(spec/def ::evaluation-context
  (spec/keys :req [::gathered-facts]))


(defn- get-missing-facts
  [condition facts]
  (set (filter (fn [fact-name]
                 (not (contains? facts
                                 fact-name)))
               (keys condition))))


(defn- condition-matches-facts?
  [condition facts]
  (set/subset? (set condition)
               (set facts)))


(defn- inline-rules
  [rules]
  (vec (reduce (fn [inlined-rules {:keys [::rule/actions
                                          ::rule/conditions
                                          ::rule/name]}]
                 (into inlined-rules
                       (map (fn [condition]
                              {::rule/actions   actions
                               ::rule/condition condition
                               ::rule/name      name})
                            conditions)))
               []
               rules)))


(defn evaluate
  [rules
   evaluation-context]
  (if (spec/valid? ::rule/rules
                   rules)
    (if (spec/valid? ::evaluation-context
                     evaluation-context)
      (let [inlined-rules (inline-rules rules)
            {:keys [::gathered-facts]} evaluation-context]
        (reduce (fn [{:keys [::index
                             ::result] :as acc}
                     {:keys [::rule/actions
                             ::rule/condition
                             ::rule/name] :as inlined-rule}]
                  (let [missing-facts (get-missing-facts condition
                                                         gathered-facts)]
                    (if (empty? missing-facts)
                      (if (condition-matches-facts? condition
                                                    gathered-facts)
                        (reduced (assoc result
                                   ::finished?
                                   true
                                   ::matched-rule
                                   inlined-rule))
                        (if (< (inc index)
                               (count inlined-rules))
                          (update acc
                                  ::index
                                  inc)
                          (reduced (assoc result
                                     ::finished?
                                     true))))
                      (reduced (assoc result
                                 ::requested-facts missing-facts)))))
                {::index  0
                 ::result {::finished?       false
                           ::gathered-facts  gathered-facts
                           ::matched-rule    nil
                           ::requested-facts #{}}}
                inlined-rules))
      (throw (AssertionError. (str "invalid evaluation context schema: "
                                   (spec/explain-str ::evaluation-context
                                                     evaluation-context)))))
    (throw (AssertionError. (str "invalid rules schema: "
                                 (spec/explain-str ::rule/rules
                                                   rules))))))
