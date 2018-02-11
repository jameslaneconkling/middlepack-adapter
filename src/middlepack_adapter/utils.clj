(ns middlepack-adapter.utils
  (:require [middlepack-adapter.range :refer [range->list]]))

(defn zipmap-all-vals
  [keys vals]
  (zipmap keys (concat vals (repeat (- (count keys) (count vals)) nil))))

(defn format-types-response
  [response]
  (->> response
       (map (comp
             #(dissoc % :classLabel)
             #(assoc % :class (.toString (% :class)))
             #(assoc % :label (get-in % [:classLabel :string]))))
       (into [])))

(defn format-properties-response
  [response]
  (->> response
       (map (comp
             #(dissoc % :propertyLabel)
             #(assoc % :property (.toString (% :property)))
             #(assoc % :label (get-in % [:propertyLabel :string]))))
       (into [])))

(defn format-triples-response
  [subject predicate range response]
  (->> response
       (map (comp
             #(dissoc % :objectLabel)
             #(assoc %
                     :object (.toString (% :object))
                     :label (get-in % [:objectLabel :string])
                     :subject subject
                     :predicate predicate)))
       (zipmap-all-vals (range->list range))))

(defn format-search-response
  [response]
  (->> response
       (map (comp
             #(dissoc % :subjectLabel)
             #(assoc %
                     :subject (.toString (% :subject))
                     :label (get-in % [:subjectLabel :string]))))))

(defn unnest-range-set
  [{:keys [subject predicate ranges]}]
  (map #(hash-map :subject subject
                  :predicate predicate
                  :range %) ranges))
