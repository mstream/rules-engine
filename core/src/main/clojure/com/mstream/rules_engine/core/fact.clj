(ns com.mstream.rules-engine.core.fact
  (:require [clojure.set :as set]
            [clojure.spec.alpha :as spec]))


(spec/def ::name
  string?)


(spec/def ::value
  boolean?)


(spec/def ::facts
  (spec/map-of ::name
               ::value))


(spec/fdef fact-combinations
           :args (spec/cat :fact-names
                           (spec/coll-of ::name
                                         :into
                                         #{})))


(spec/fdef expand-facts
           :args (spec/cat :facts
                           ::facts
                           :fact-names
                           (spec/coll-of ::name)))


(defn fact-combinations
  "returns a set of all possible combination of facts"
  ([fact-names]
   (if (empty? fact-names)
     #{}
     (fact-combinations fact-names
                        {})))
  ([fact-names partial-combination]
   (if (empty? fact-names)
     #{partial-combination}
     (let [fact-name (first fact-names)]
       (into (fact-combinations (disj fact-names
                                      fact-name)
                                (assoc partial-combination
                                  fact-name
                                  false))
             (fact-combinations (disj fact-names
                                      fact-name)
                                (assoc partial-combination
                                  fact-name
                                  true)))))))


(defn expand-facts
  "returns a set of all fact combinations for the given facts"
  [facts fact-names]
  (set (filter
         (fn [fact-combination]
           (set/subset? facts
                        (set fact-combination)))
         (fact-combinations fact-names))))
