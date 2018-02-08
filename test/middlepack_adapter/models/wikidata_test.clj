(ns middlepack-adapter.models.wikidata-test
  (:require [mount.core :as mount]
            [middlepack-adapter.models.wikidata :as wikidata]
            [clojure.test :refer [deftest
                                  testing
                                  is]]))

(mount/start)

(deftest wikidata-model
  (testing "wikidata get-types"
    (let [response (wikidata/get-types 5)]
      (is (every?
           #(= (set (keys %)) #{:class :label})
           response))

      #_(is (= (count response) 5))))

  (testing "wikidata get-properties"
    (let [response (wikidata/get-properties-for-type
                    (-> (wikidata/get-static-types) first :class)
                    5)]
      (is (every?
           #(= (set (keys %)) #{:property :label})
           response))

      #_(is (= (count response) 5)))))
