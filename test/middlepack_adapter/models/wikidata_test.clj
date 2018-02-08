(ns middlepack-adapter.models.wikidata-test
  (:require [middlepack-adapter.models.wikidata :as wikidata]
            [clojure.test :refer [deftest
                                  testing
                                  is]]))

(deftest wikidata-model
  (testing "wikidata get-properties"
    (let [response (wikidata/get-properties-for-type
                    (-> (wikidata/get-static-types) first :class)
                    5)]
      (is (every?
           #(= (set (keys %)) #{:property :label})
           response))

                                        ; specifying limit does not work. see github issue:  https://github.com/joelkuiper/yesparql/issues/5
      #_(is (= (count response) 5))))

  (testing "wikidata get-types"
    (let [response (wikidata/get-types 5)]
      (is (every?
           #(= (set (keys %)) #{:class :label})
           response))

                                        ; specifying limit does not work. see github issue:  https://github.com/joelkuiper/yesparql/issues/5
      #_(is (= (count response) 5)))))
