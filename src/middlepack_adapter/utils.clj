(ns middlepack-adapter.utils
  (:require [middlepack-adapter.range :refer [range->list]]))


(defn pad
  [n coll val]
  (take n (concat coll (repeat val))))

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
  (let [indices (range->list range)
        padded-response (pad (count indices) response nil)]
    (map
     (defn transform [response-triple, index]
       (if (nil? response-triple)
         {:subject subject :predicate predicate :index index}
         ((comp
           #(dissoc % :objectLabel)
           #(assoc %
                   :object (.toString (% :object))
                   :label (get-in % [:objectLabel :string])
                   :subject subject
                   :predicate predicate
                   :index index)) response-triple)))
     padded-response
     indices)))

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
