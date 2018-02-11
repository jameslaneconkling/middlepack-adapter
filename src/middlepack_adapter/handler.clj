(ns middlepack-adapter.handler
  (:require [mount.core :as mount]
            [compojure.core :refer :all]
            [compojure.route :refer [not-found]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]
            [middlepack-adapter.models.dbpedia :as dbpedia]
            [middlepack-adapter.models.wikidata :as wikidata]))


(defn valid-repository? [repository]
  (some #(= repository %) ["dbpedia" "wikidata"]))

(defn type-handler
  [repository]
  (case repository
    "dbpedia" (response (dbpedia/get-static-types))
    "wikidata" (response (wikidata/get-static-types))
    (not-found (response (:message "Repository Does Not Exist")))))


(defn properties-handler
  [repository type-label]
  (case repository
    "dbpedia" (if-let [result (dbpedia/get-properties-for-type-label type-label 10)]
                (response result)
                (not-found (response {:message "Type Does Not Exist"})))
    "wikidata" (if-let [result (wikidata/get-properties-for-type-label type-label 10)]
                 (response result)
                 (not-found (response {:message "Type Does Not Exist"})))
    (not-found (response {:message "Repository Does Not Exist"}))))


(defn triple-handler
  [repository triples]
  (case repository
    "dbpedia" (response (dbpedia/get-triples triples))
    "wikidata" (response (dbpedia/get-triples triples))
    (not-found (response {:message "Repository Does Not Exist"}))))


(defroutes app-routes
  (GET "/:repository/types"
       [repository]
       (type-handler repository))
  (GET "/:repository/properties/:type-label"
       [repository type-label]
       ;; TODO - validate repository in middleware
       (properties-handler repository type-label))
  (POST "/:repository/facts"
        [repository :as {:keys [body]}]
        (triple-handler repository body))
  (not-found (response {:message "Not Found"})))


(def app
  (do
    (mount/start)
    (-> app-routes
        wrap-json-response
        (wrap-json-body {:keywords? true})
        ;; disable CSRF protection
        (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false)))))
