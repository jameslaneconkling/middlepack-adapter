(ns middlepack-adapter.routes.wikidata
  (:require [yesparql.core :refer [defquery]]
            [middlepack-adapter.utils :refer [format-types-response
                                               format-properties-response]]))


(defquery get-types-query "sparql/wikidata/get-types.sparql"
  {:connection  "https://query.wikidata.org/sparql"})

(defquery get-properties-for-type-query "sparql/wikidata/get-properties-for-type.sparql"
  {:connection  "https://query.wikidata.org/sparql"})


(defn get-types [limit]
  (with-open [result (get-types-query
                      {:limit limit})]
    (format-types-response result)))

(defn get-static-types []
  [{ :class "http://www.wikidata.org/entity/Q43229" :class-label "organization"}
   { :class "http://www.wikidata.org/entity/Q783794" :class-label "company"}
   { :class "http://www.wikidata.org/entity/Q215627" :class-label "person" }
   { :class "http://www.wikidata.org/entity/Q1190554" :class-label "occurance" }
   { :class "http://www.wikidata.org/entity/Q17334923" :class-label "location" }
   { :class "http://www.wikidata.org/entity/Q386724" :class-label "work" }])

(defn get-properties-for-type [type limit]
  (with-open [result (get-properties-for-type-query
                      {:limit limit
                       :bindings {:type (java.net.URI. type)}})]
    (format-properties-response result)))
