(ns middlepack-adapter.models.dbpedia
  (:require [clojure.java.io :refer [file]]
            [grafter.rdf.repository :refer [sparql-repo]]
            [grafter.rdf.sparql :refer [query]]
            [middlepack-adapter.utils :refer [format-types-response
                                              format-properties-response]])
  (:import [java.net URI]))

;; see [Ontology Classes](http://mappings.dbpedia.org/server/ontology/classes/)
;; for list of classes, properties, and property domains/ranges
(defn get-types
  [limit]
  (let [response (query "sparql/dbpedia/get-types.sparql"
                        (sparql-repo "http://dbpedia.org/sparql"))]
    (format-types-response response)))

(defn get-static-types
  []
  [{ :class "http://dbpedia.org/ontology/Organisation" :label "organization"}
   { :class "http://dbpedia.org/ontology/Company" :label "company"}
   { :class "http://dbpedia.org/ontology/Person" :label "person"}
   { :class "http://dbpedia.org/ontology/Event" :label "event"}
   { :class "http://dbpedia.org/ontology/Place" :label "place"}
   { :class "http://dbpedia.org/ontology/Work" :label "work"}])


(defn get-properties-for-type
  [type limit]
  (let [response (query "sparql/dbpedia/get-properties-for-type.sparql"
                        {:limits limit
                         :type (URI. type)}
                        (sparql-repo "http://dbpedia.org/sparql"))]
    (format-properties-response response)))


#_(count (get-properties-for-type
        (:class (last (get-static-types)))
        4))
