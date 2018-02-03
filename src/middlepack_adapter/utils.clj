(ns middlepack-adapter.utils
  (:require [clojure.set :refer [rename-keys]]))


(defn format-types-response [response]
  (->> response
       (map #(rename-keys % {"class" :class "classLabel" :label}))
       (map #(assoc % :class-label (get-in % [:class-label :value])))
       (into [])))

(defn format-properties-response [response]
  (->> response
       (map (comp
             #(assoc % :property-label (get-in % [:property-label :value]))
             #(rename-keys % {"property" :property "propertyLabel" :label})))
       (into [])))
