(ns middlepack-adapter.core
  (:require [mount.core :as mount]
            [middlepack-adapter.models.dbpedia :as dbpedia]
            [middlepack-adapter.models.wikidata :as wikidata])
  (:gen-class))


(defn printVec [vec]
  (doseq [item vec] (println item)))

(defn -main
  [& args]
  (mount/start)
  (printVec (wikidata/get-types 5)))

