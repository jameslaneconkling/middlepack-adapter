(ns middlepack-adapter.repository
  (:require [clojure.java.io :refer [file]]
            [mount.core :refer [NotStartedState]]
            [grafter.rdf.repository :refer [ToConnection]])
  (:import [org.eclipse.rdf4j.repository Repository]
           [org.eclipse.rdf4j.repository.sail SailRepository]
           [org.eclipse.rdf4j.repository.sparql SPARQLRepository]
           [org.eclipse.rdf4j.sail.memory MemoryStore]
           [org.eclipse.rdf4j.sail.nativerdf NativeStore]))


(extend-protocol ToConnection
  Repository
  (->connection
    [^Repository repo]
    (.getConnection repo)))


(defn memory-repo
  "TODO - allow inferencing"
  ([]
   (doto (SailRepository. (MemoryStore.))
     (.initialize)))
  ([filePath]
   (doto (SailRepository. (MemoryStore. (file filePath)))
     (.initialize))))


(defn native-repo
  "TODO - allow inferencing"
  ([filePath] (native-repo filePath "spoc,posc,cosp"))
  ([filePath indexes]
   (doto (SailRepository. (NativeStore. (file filePath) indexes))
     (.initialize))))


(defn sparql-repo
  ([url]
   (doto (SPARQLRepository. url)
     (.initialize))))


(defn initialized?
  [repo]
  (not (instance? NotStartedState repo)))
