(ns middlepack-adapter.models.wikidata
  (:require [mount.core :refer [defstate]]
            [grafter.rdf.repository :refer [sparql-repo
                                            shutdown]]
            [grafter.rdf.sparql :as sparql :refer [query]]
            [middlepack-adapter.utils :refer [format-types-response
                                              format-properties-response]])
  (:import [java.net URI]))


(defstate wikidata-repo
  :start (do
           (println "initialize wikidata repository")
           (sparql-repo "https://query.wikidata.org/sparql"))
  :stop (do
          (println "shut down wikidata repository")
          (shutdown wikidata-repo)))


;; see [List of Properties](https://www.wikidata.org/wiki/Wikidata:List_of_properties/all)
;; for list of common classes, their properties, and property domain/ranges
;; NOTE: using (wdt:P279)* [subclass] times out"
(defn get-types
  [limit]
  (let [response (query "sparql/wikidata/get-types.sparql"
                        {::sparql/limits {:limit limit}}
                        wikidata-repo)]
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
                        {::sparql/limits {:limit limit}
                         :type (URI. type)}
                        wikidata-repo)]
    (format-properties-response response)))
