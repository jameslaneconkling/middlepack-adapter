(ns middlepack-adapter.models.dbpedia-test
  (:require [middlepack-adapter.models.dbpedia :as dbpedia]
            [clojure.test :refer [deftest
                                  testing
                                  is]]))


(deftest dbpedia-model
  (testing "dbpedia get-properties"
    (let [response (dbpedia/get-properties-for-type
                    (-> (dbpedia/get-static-types) (nth 0) (:class))
                    5)]
      (is (every?
           #(= (keys %) [:property :label])
           response))

                                        ; specifying limit does not work. see github issue:  https://github.com/joelkuiper/yesparql/issues/5
      #_(is (= (count response) 5))))

  (testing "dbpedia get-types"
    (let [response (dbpedia/get-types 5)]
      (is (every?
           #(= (keys %) [:class :label])
           response))

                                        ; specifying limit does not work. see github issue:  https://github.com/joelkuiper/yesparql/issues/5
      #_(is (= (count response) 5)))))
