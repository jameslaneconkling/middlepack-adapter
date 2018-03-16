(ns middlepack-adapter.utils
  (:require [middlepack-adapter.range :refer [range->list]]
            [grafter.rdf.protocols :as protocols :refer [raw-value
                                                         #_LangString]])
  (:import [java.net URI]
           [grafter.rdf.protocols LangString]))


(defprotocol TripleObject
  (stringify [this]))

(extend-protocol TripleObject
  URI
  (stringify [this] (.toString this))
  LangString
  (stringify [this] (str "\"" (raw-value this) "\""))
  Object
  (stringify [this] (.toString this)))


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
     (fn [response-triple, index]
       (if (nil? response-triple)
         {:subject subject :predicate predicate :index index}
         ((comp
           #(dissoc % :objectLabel)
           #(assoc %
                   :object (stringify (% :object))
                   :label (raw-value (:objectLabel %))
                   :subject subject
                   :predicate predicate
                   :index index)) response-triple)))
     padded-response
     indices)))

(defn format-search-response
  [response range]
  (let [indices (range->list range)]
    (map (fn [triple index]
           (-> triple
               (assoc :subject (.toString (triple :subject))
                      :label (get-in triple [:subjectLabel :string])
                      :index index)
               (dissoc :subjectLabel))) response indices)))


(defn unnest-range-set
  [{:keys [subject predicate ranges]}]
  (map #(hash-map :subject subject
                  :predicate predicate
                  :range %) ranges))
