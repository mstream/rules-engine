(ns com.mstream.rules-engine.java.evaluation-result)


(defrecord EvaluationResult
  [finished?
   gatheredFacts
   matchedRule
   requestedFacts]
  com.mstream.rulesengine.core.EvaluationResult
  (isFinished [_]
    finished?)
  (getGatheredFacts [_]
    gatheredFacts)
  (getMatchedRule [_]
    matchedRule)
  (getRequestedFacts [_]
    requestedFacts)
  (toString [_]
    (str {:finished?      finished?
          :gatheredFacts  gatheredFacts
          :matchedRule    matchedRule
          :requestedFacts requestedFacts})))
