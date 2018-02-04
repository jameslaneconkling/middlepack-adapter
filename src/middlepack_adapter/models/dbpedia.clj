(ns middlepack-adapter.models.dbpedia
  (:require [yesparql.core :refer [defquery]]
            [middlepack-adapter.utils :refer [format-types-response
                                               format-properties-response]]))


(defquery get-types-query "sparql/dbpedia/get-types.sparql"
  {:connection "http://dbpedia.org/sparql"})

(defquery get-properties-for-type-query "sparql/dbpedia/get-properties-for-type.sparql"
  {:connection  "http://dbpedia.org/sparql"})


(defn get-types [limit]
  (with-open [result (get-types-query
                      {:limit limit})]
    (format-types-response result)))

(defn get-static-types []
  [{ :class "http://dbpedia.org/ontology/Organisation" :label "organization"}
   { :class "http://dbpedia.org/ontology/Company" :label "company"}
   { :class "http://dbpedia.org/ontology/Person" :label "person"}
   { :class "http://dbpedia.org/ontology/Event" :label "event"}
   { :class "http://dbpedia.org/ontology/Place" :label "place"}
   { :class "http://dbpedia.org/ontology/Work" :label "work"}])

(defn get-properties-for-type [type limit]
  (with-open [result (get-properties-for-type-query
                      {:limit limit
                       :bindings {:type (java.net.URI. type)}})]
    (format-properties-response result)))

(get-types 10)
