(ns com.mstream.rules-engine.java.rule)


(defrecord Rule
  [name condition actions]
  com.mstream.rulesengine.core.Rule
  (getName [_]
    name)
  (getCondition [_]
    condition)
  (getActions [_]
    actions)
  (toString [_]
    (str {:name      name
          :condition condition
          :actions   actions})))
