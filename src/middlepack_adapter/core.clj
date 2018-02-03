(ns middlepack-adapter.core
  (:require [:middlepack-adapter.routes.dbpedia :refer [get-dbpedia-types
                                                        #_get-wikidata-types]])
  (:gen-class))


(defn printVec [vec]
  (doseq [item vec] (println item)))

(defn -main
  [& args]
  (doseq [result (get-dbpedia-types 20)] (println result)))
