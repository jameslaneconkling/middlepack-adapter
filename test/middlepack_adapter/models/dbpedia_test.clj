(ns middlepack-adapter.models.dbpedia-test
  (:require [middlepack-adapter.models.dbpedia :as dbpedia]
            [clojure.test :refer [deftest
                                  testing
                                  is]]))


(deftest dbpedia-model
  (testing "dbpedia get-types"
    (let [response (dbpedia/get-types 5)]
      (is (every?
           #(= (set (keys %)) #{:class :label})
           response))

      #_(is (= (count response) 5))))

  (testing "dbpedia get-properties"
    (let [response (dbpedia/get-properties-for-type
                    (-> (dbpedia/get-static-types) first :class)
                    5)]
      (is (every?
           #(= (set (keys %)) #{:property :label})
           response))

      #_(is (= (count response) 5)))))
