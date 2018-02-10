(ns middlepack-adapter.scraper
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn value->index
  [value]
  (case value
    :subject 0
    :predicate 1
    :object 2))

(defn parse-triple [line pattern value]
  (let [value (-> line
                  (str/split #" ")
                  (nth (value->index value)))]
    (when (re-find pattern value)
      value)))

(defn extract-triples
  [file pattern & {:keys [value limit] :or {value :predicate} }]
  (with-open [f (-> file
                    io/file
                    io/reader)]
    (if limit
      (into [] (take limit (keep #(parse-triple % pattern value) (line-seq f))))
      (into [] (keep #(parse-triple % pattern value) (line-seq f))))))


(extract-triples
 "/Users/jamesconkling/Documents/Projects/middlepack-adapter/resources/wikidata-sample.nt"
 #"http://www.wikidata.org/entity/P1036c"
 :value :predicate
 :limit 10)
