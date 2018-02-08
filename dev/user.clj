;; how to inject this into the repl ns
;; (use '[user])
;; (refer 'user)
(ns user
  (:require
   [clojure.pprint :refer (pprint)]
   [clojure.repl :refer :all]
   [clojure.string :as str]
   [clojure.test :refer [run-tests run-all-tests]]
   [mount.core :as mount]
   #_[clojure.tools.namespace.repl :refer [refresh refresh-all]]))

(def qwerty :qwerty)
