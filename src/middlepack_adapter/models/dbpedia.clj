(ns middlepack-adapter.models.dbpedia
  (:require [mount.core :refer [defstate]]
            [grafter.rdf.repository :refer [sparql-repo
                                            shutdown]]
            [grafter.rdf.sparql :as sparql :refer [query]]
            [middlepack-adapter.range :refer [range->offset-limit]]
            [middlepack-adapter.utils :refer [format-types-response
                                              format-properties-response
                                              format-triples-response
                                              format-search-response
                                              unnest-range-set]])
  (:import [java.net URI]))


(defstate dbpedia-repo
  :start (do
           (println "initialize dbpedia repository")
           (sparql-repo "http://dbpedia.org/sparql"))
  :stop (do
          (println "shut down dbpedia repository")
          (shutdown dbpedia-repo)))

(defn inferPredicate
  [predicate]
  (case predicate
    "http://www.w3.org/2004/02/skos/core#prefLabel" "http://www.w3.org/2000/01/rdf-schema#label"
    predicate))

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


(defn get-properties-for-type-label
  [type-label limit]
  (when-let [type-uri (->> (get-static-types)
                           (filter #(= (:label %) type-label))
                           first
                           :class)]
    (get-properties-for-type type-uri limit)))


(defn get-triple
  [{:keys [subject predicate range] :as triple}]
  (let [{:keys [offset limit]} (range->offset-limit range)
        inferredPredicate (inferPredicate predicate)
        response (query "sparql/dbpedia/get-triples.sparql"
                        {::sparql/offsets {:offset offset}
                         ::sparql/limits {:limit limit}
                         :subj (URI. subject)
                         :pred (URI. inferredPredicate)} ; NOTE - predicate is a reserved word in grafter SPARQL bindings [why?]
                        dbpedia-repo)]
    (format-triples-response subject predicate range response)))


(defn get-triples
  [triples]
  (->> triples
       (mapcat unnest-range-set)
       (pmap get-triple)
       flatten))


(defn get-search-for-range
  [type range]
  (let [{:keys [offset limit]} (range->offset-limit range)
        response (query "sparql/dbpedia/get-search.sparql"
                        {::sparql/offsets {:offset offset}
                         ::sparql/limits {:limit limit}
                         :type (URI. type)}
                        dbpedia-repo)]
    (format-search-response response range)))


(defn get-search
  [{:keys [type ranges]}]
  (->> ranges
       (pmap (partial get-search-for-range type))
       (apply concat)))
