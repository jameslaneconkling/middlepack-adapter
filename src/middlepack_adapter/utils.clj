(ns middlepack-adapter.utils
  (:require [clojure.set :refer [rename-keys]]))


(defn format-types-response [response]
  (->> response
       (map (comp
             #(assoc % :label (get-in % [:label :value]))
             #(rename-keys % {"class" :class "classLabel" :label})))
       (into [])))

(defn format-properties-response [response]
  (->> response
       (map (comp
             #(assoc % :label (get-in % [:label :value]))
             #(rename-keys % {"property" :property "propertyLabel" :label})))
       (into [])))
