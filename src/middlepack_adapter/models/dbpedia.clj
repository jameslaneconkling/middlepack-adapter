(ns middlepack-adapter.models.dbpedia
  (:require [mount.core :refer [defstate]]
            [grafter.rdf.repository :refer [sparql-repo
                                            shutdown]]
            [grafter.rdf.sparql :as sparql :refer [query]]
            [middlepack-adapter.utils :refer [format-types-response
                                              format-properties-response]])
  (:import [java.net URI]))


(defstate dbpedia-repo
  :start (do
           (println "initialize dbpedia repository")
           (sparql-repo "http://dbpedia.org/sparql"))
  :stop (do
          (println "shut down dbpedia repository")
          (shutdown dbpedia-repo)))

;; see [Ontology Classes](http://mappings.dbpedia.org/server/ontology/classes/)
;; for list of classes, properties, and property domains/ranges
(defn get-types
  [limit]
  (let [response (query "sparql/dbpedia/get-types.sparql"
                        {::sparql/limits {:limit limit}}
                        dbpedia-repo)]
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
                        {::sparql/limits {:limit limit}
                         :type (URI. type)}
                        dbpedia-repo)]
    (format-properties-response response)))

(defn get-type-uri-from-label
  [type-label]
  (->> (get-static-types)
                     (filter #(= (:label %) type-label))
                     first
                     :class))
