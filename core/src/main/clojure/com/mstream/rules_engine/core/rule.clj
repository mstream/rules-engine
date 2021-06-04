(ns com.mstream.rules-engine.core.rule
  (:require [clojure.data.json :as json]
            [clojure.edn :as edn]
            [clojure.spec.alpha :as spec]
            [com.mstream.rules-engine.core.fact :as fact]))


(spec/def ::action
  string?)


(spec/def ::actions
  (spec/* ::action))


(spec/def ::condition
  ::fact/facts)


(spec/def ::conditions
  (spec/+ ::condition))


(spec/def ::name
  string?)


(spec/def ::rule
  (spec/keys :req [::actions
                   ::conditions
                   ::name]))


(spec/def ::rules
  (spec/* ::rule))


(spec/fdef rules-actions
           :args (spec/cat :rules
                           ::rules))


(defn rules-actions
  "returns a set of actions occurring in rules"
  [rules]
  (reduce (fn [rules-actions {:keys [::actions]}]
            (into rules-actions
                  actions))
          #{}
          rules))


(defn rules-fact-names
  "returns a set of fact names occurring in rules conditions"
  [rules]
  (reduce (fn [rules-fact-names {:keys [::conditions]}]
            (into rules-fact-names
                  (reduce (fn [facts condition]
                            (into facts
                                  (keys condition)))
                          #{}
                          conditions)))
          #{}
          rules))


(defn- translate-keywords
  "returns rules with full name keyword keys"
  [rules]
  (mapv (fn [{:keys [:actions
                     :conditions
                     :name]}]
          {::actions    actions
           ::conditions conditions
           ::name       name})
        rules))


(defn jsonStr->rules
  [rules-string]
  (translate-keywords (map (fn [rule]
                             (into {}
                                   (map (fn [[k v]]
                                          [(keyword k)
                                           v])
                                        rule)))
                           (json/read-str rules-string))))


  (defn ednStr->rules
    [rules-string]
    (translate-keywords (edn/read-string rules-string)))



