(ns com.mstream.rules-engine.java.validation-result)


(defrecord ValidationResult
  [errors
   warnings
   actions
   facts]
  com.mstream.rulesengine.core.ValidationResult
  (getErrors [_]
    errors)
  (getWarnings [_]
    warnings)
  (getActions [_]
    actions)
  (getFacts [_]
    facts)
  (toString [_]
    (str {:errors   errors
          :warnings warnings
          :actions  actions
          :facts    facts})))
