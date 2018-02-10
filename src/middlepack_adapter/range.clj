(ns middlepack-adapter.range)

(defprotocol IRange
  "..."
  (range->list [item] "...")
  (range->offset-limit [item] "..."))


(extend-protocol IRange
  clojure.lang.PersistentArrayMap
  (range->list
    [{:keys [from to] :or {from 0}}]
    (range from (inc to)))
  (range->offset-limit
    [{:keys [from to] :or {from 0}}]
    (hash-map :offset from :limit (- (inc to) from)))

  java.lang.Integer
  (range->list
    [element]
    (vector element))
  (range->offset-limit
    [element]
    (hash-map :offset element :limit 1))

  ;; TODO - is there a more concise pattern?
  java.lang.Long
  (range->list
    [element]
    (vector element))
  (range->offset-limit
    [element]
    (hash-map :offset element :limit 1)))
