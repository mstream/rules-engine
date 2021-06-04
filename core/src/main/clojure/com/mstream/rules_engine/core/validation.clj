(ns com.mstream.rules-engine.core.validation
  (:require [clojure.set :as set]
            [clojure.spec.alpha :as spec]
            [com.mstream.rules-engine.core.fact :as fact]
            [com.mstream.rules-engine.core.rule :as rule]))



(defn- expand-rule
  "retuns a map representing the rule with its conditions expanded"
  [rule fact-names]
  (assoc rule
    ::rule/conditions
    (reduce (fn [conditions condition]
              (into conditions condition))
            #{}
            (map (fn [condition]
                   (fact/expand-facts condition
                                      fact-names))
                 (::rule/conditions rule)))))


(defn- expand-rules
  "returns a sequence of rules with their conditions expanded"
  [rules fact-names]
  (map (fn [rule]
         (expand-rule rule
                      fact-names))
       rules))


(defn- find-missing-conditions
  "returns a set of missing conditions"
  [rules fact-combinations]
  (apply set/difference
         fact-combinations
         (map ::rule/conditions
              rules)))


(defn- get-actions-by-rules
  "returns a map of actions and rules where they occur"
  [rules]
  (reduce (fn [actions-by-rules {rule-actions ::rule/actions}]
            (assoc actions-by-rules
              rule-actions
              (vec (map ::rule/name
                        (filter (fn [{other-rule-actions ::rule/actions}]
                                  (= rule-actions
                                     other-rule-actions))
                                rules)))))
          {}
          rules))


(defn- get-conditions-by-rules
  "returns a map of conditions and rules where they occur"
  [rules fact-combinations]
  (reduce (fn [conditions-by-rules fact-combination]
            (assoc conditions-by-rules
              fact-combination
              (vec (map ::rule/name
                        (filter (fn [{:keys [::rule/conditions]}]
                                  (contains? conditions
                                             fact-combination))
                                rules)))))
          {}
          fact-combinations))


(defn- find-duplicated-actions
  "returns a map of duplicated actions an their rules"
  [rules]
  (into {}
        (filter (fn [[_ rules]]
                  (> (count rules)
                     1)))
        (get-actions-by-rules rules)))


(defn- find-duplicated-conditions
  "returns a map of duplicated conditions an their rules"
  [rules fact-combinations]
  (into {}
        (filter (fn [[_ rules]]
                  (> (count rules)
                     1)))
        (get-conditions-by-rules rules
                                 fact-combinations)))


(defn- find-duplicated-rule-names
  "returns a set of duplicated rule names"
  [rules]
  (set (map first
            (filter (fn [[_ frequency]]
                      (> frequency
                         1))
                    (frequencies (map ::rule/name
                                      rules))))))


(defn- find-rules-without-actions
  "returns a set of names of rules which do not have any actions"
  [rules]
  (set (map ::rule/name
            (filter (fn [{:keys [::rule/actions]}]
                      (empty? actions))
                    rules))))

(defn validate-rules
  [rules
   {:keys [::ignored-warnings]}]
  (if (spec/valid? ::rule/rules
                   rules)
    (let [fact-names (rule/rules-fact-names rules)
          actions (rule/rules-actions rules)
          fact-combinations (fact/fact-combinations fact-names)
          expanded-rules (expand-rules rules
                                       fact-names)
          missing-conditions (find-missing-conditions expanded-rules
                                                      fact-combinations)
          duplicated-actions (find-duplicated-actions expanded-rules)
          duplicated-conditions (find-duplicated-conditions expanded-rules
                                                            fact-combinations)
          duplicated-rule-names (find-duplicated-rule-names expanded-rules)
          rules-without-actions (find-rules-without-actions expanded-rules)]
      {::errors   {}
       ::info     {::facts   fact-names
                   ::actions actions}
       ::warnings (into {}
                        (filter (fn [[k v]]
                                  (and (not (empty? v))
                                       (not (contains? ignored-warnings
                                                       k))))
                                {::missing-conditions    missing-conditions
                                 ::duplicated-actions    duplicated-actions
                                 ::duplicated-conditions duplicated-conditions
                                 ::duplicated-rule-names duplicated-rule-names
                                 ::rules-without-actions rules-without-actions}))})
    {::errors {::invalid-schema (spec/explain-data ::rule/rules
                                                   rules)}}))

