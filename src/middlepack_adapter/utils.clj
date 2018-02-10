(ns middlepack-adapter.utils)


(defn format-types-response [response]
  (->> response
       (map (comp
             #(dissoc % :classLabel)
             #(assoc % :class (.toString (% :class)))
             #(assoc % :label (get-in % [:classLabel :string]))))
       (into [])))

(defn format-properties-response [response]
  (->> response
       (map (comp
             #(dissoc % :propertyLabel)
             #(assoc % :property (.toString (% :property)))
             #(assoc % :label (get-in % [:propertyLabel :string]))))
       (into [])))

(defn format-triples-response
  [subject predicate response]
  (->> response
       (map (comp
             #(dissoc % :objectLabel)
             #(assoc %
                     :object (.toString (% :object))
                     :label (get-in % [:objectLabel :string])
                     :subject subject
                     :predicate predicate)))
       (into [])))
