(ns middlepack-adapter.models.dbpedia
  (:require [mount.core :refer [defstate]]
            [grafter.rdf.repository :refer [sparql-repo
                                            shutdown]]
            [grafter.rdf.sparql :as sparql :refer [query]]
            [middlepack-adapter.utils :refer [format-types-response
                                              format-properties-response
                                              format-triples-response]])
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


(defn get-properties-for-type-label
  [type-label limit]
  (when-let [type-uri (->> (get-static-types)
                           (filter #(= (:label %) type-label))
                           first
                           :class)]
    (get-properties-for-type type-uri limit)))


(defn get-triple
  [subject predicate range]
  (let [response (query "sparql/dbpedia/get-triples.sparql"
                        {::sparql/limits {:limit 10}
                         :subj (URI. subject)
                         :pred (URI. predicate)}
                        dbpedia-repo)]
    (format-triples-response subject predicate response)))


(defn get-triples
  [triples]
  (pmap
   (fn [{:keys [subject predicate range]}]
     (get-triple subject predicate range))
   triples))

#_(def triples
  [{:subject "http://dbpedia.org/resource/Lorine_Livington_Pruette"
    :predicate "http://www.w3.org/2002/07/owl#sameAs"
    :range {:to 5}}
   {:subject "http://dbpedia.org/resource/William_Bagot,_2nd_Baron_Bagot"
    :predicate "http://xmlns.com/foaf/0.1/depiction"
    :range 0}])

#_(get-triples triples)
