(ns middlepack-adapter.routes.dbpedia
  (:require [yesparql.core :refer [defquery]]
            [middlepack-adapter.utils :refer [format-types-response
                                               format-properties-response]]))


(defquery get-dbpedia-types-query "sparql/dbpedia/get-types.sparql"
  {:connection "http://dbpedia.org/sparql"})

(defquery get-dbpedia-properties-for-type-query "sparql/dbpedia/get-properties-for-type.sparql"
  {:connection  "http://dbpedia.org/sparql"})


;; Dbpedia
(defn get-dbpedia-types [limit]
  (with-open [result (get-dbpedia-types-query
                      {:limit limit})]
    (format-properties-response result)))

(defn get-select-dbpedia-types []
  [{ :class "http://dbpedia.org/ontology/Organisation" :class-label "organization"}
   { :class "http://dbpedia.org/ontology/Company" :class-label "company"}
   { :class "http://dbpedia.org/ontology/Person" :class-label "person"}
   { :class "http://dbpedia.org/ontology/Event" :class-label "event"}
   { :class "http://dbpedia.org/ontology/Place" :class-label "place"}
   { :class "http://dbpedia.org/ontology/Work" :class-label "work"}])

(defn get-dbpedia-properties-for-type [type limit]
  (with-open [result (get-dbpedia-properties-for-type-query
                      {:limit limit
                       :bindings {:type (java.net.URI. type)}})]
    (format-properties-response result)))

#_(get-dbpedia-properties-for-type (get-in (get-select-dbpedia-types) [0 :class]) 5)
