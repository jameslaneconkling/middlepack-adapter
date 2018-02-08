(ns middlepack-adapter.models.wikidata
  (:require [grafter.rdf.repository :refer [sparql-repo]]
            [grafter.rdf.sparql :refer [query]]
            [middlepack-adapter.utils :refer [format-types-response
                                              format-properties-response]])
  (:import [java.net URI]))


;; see [List of Properties](https://www.wikidata.org/wiki/Wikidata:List_of_properties/all)
;; for list of common classes, their properties, and property domain/ranges
;; NOTE: using (wdt:P279)* [subclass] times out"
(defn get-types
  [limit]
  (let [response (query "sparql/wikidata/get-types.sparql"
                        {:limits limit}
                        (sparql-repo "https://query.wikidata.org/sparql"))]
    (format-types-response response)))


(defn get-static-types []
  [{ :class "http://www.wikidata.org/entity/Q43229" :label "organization"}
   { :class "http://www.wikidata.org/entity/Q783794" :label "company"}
   { :class "http://www.wikidata.org/entity/Q215627" :label "person" }
   { :class "http://www.wikidata.org/entity/Q1190554" :label "occurance" }
   { :class "http://www.wikidata.org/entity/Q17334923" :label "location" }
   { :class "http://www.wikidata.org/entity/Q386724" :label "work" }])


(defn get-properties-for-type
  [type limit]
  (let [response (query "sparql/wikidata/get-properties-for-type.sparql"
                        {:limits limit
                         :type (URI. type)}
                        (sparql-repo "https://query.wikidata.org/sparql"))]
    (format-properties-response response)))

(count (get-properties-for-type
        (:class (first (get-static-types)))
        5))
