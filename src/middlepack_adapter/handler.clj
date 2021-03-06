(ns middlepack-adapter.handler
  (:require [mount.core :as mount]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :refer [not-found]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]
            [ring.logger.timbre :refer [wrap-with-logger]]
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
    "dbpedia" (if-let [result (dbpedia/get-properties-for-type-label type-label 100)]
                (response result)
                (not-found (response {:message "Type Does Not Exist"})))
    "wikidata" (if-let [result (wikidata/get-properties-for-type-label type-label 100)]
                 (response result)
                 (not-found (response {:message "Type Does Not Exist"})))
    (not-found (response {:message "Repository Does Not Exist"}))))

(defn type-properties-handler
  [repository type]
  (case repository
    "dbpedia" (if-let [result (dbpedia/get-properties-for-type type 100)]
                (response result)
                (not-found (response {:message "Type Does Not Exist"})))
    "wikidata" (if-let [result (wikidata/get-properties-for-type type 100)]
                 (response result)
                 (not-found (response {:message "Type Does Not Exist"})))
    (not-found (response {:message "Repository Does Not Exist"}))))

(defn triple-handler
  [repository triples]
  (case repository
    "dbpedia" (response (dbpedia/get-triples triples))
    (not-found (response {:message "Repository Does Not Exist"}))))


(defn search-handler
  [repository search]
  (case repository
    "dbpedia" (response (dbpedia/get-search search))
    (not-found (response {:message "Repository Does Not Exist"}))))


(defroutes app-routes
  (GET "/:repository/types"
       [repository]
       (type-handler repository))
  (GET "/:repository/properties/:type-label"
       [repository type-label]
       ;; TODO - validate repository in middleware
       (properties-handler repository type-label))
  (POST "/:repository/properties"
        [repository :as {:keys [body]}]
        ;; TODO - validate repository in middleware
        (type-properties-handler repository (:type body)))
  (POST "/:repository/facts"
        [repository :as {:keys [body]}]
        (triple-handler repository body))
  (POST "/:repository/search"
        [repository :as {:keys [body]}]
        (search-handler repository body))
  (not-found (response {:message "Not Found"})))


(defn wrap-exception-handling
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        {:status 500 :body (:cause (Throwable->map e))}))))

(def app
  (do
    (mount/start)
    (-> app-routes
        wrap-with-logger
        wrap-json-response
        (wrap-json-body {:keywords? true})
        ;; disable CSRF protection
        (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
        wrap-exception-handling)))
