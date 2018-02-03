(ns middlepack-adapter.core
  (:require [middlepack-adapter.models.dbpedia :as dbpedia] 
            [middlepack-adapter.models.wikidata :as wikidata])
  (:gen-class))


(defn printVec [vec]
  (doseq [item vec] (println item)))

(defn -main
  [& args]
  (printVec (wikidata/get-static-types)))
