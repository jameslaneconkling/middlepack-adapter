(ns middlepack-adapter.handler
  (:require [mount.core :as mount]
            [compojure.core :refer :all]
            [compojure.route :refer [not-found]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]
            [middlepack-adapter.models.dbpedia :as dbpedia]
            [middlepack-adapter.models.wikidata :as wikidata]))


(defn valid-repository [repository]
  (some #(= repository %) ["dbpedia" "wikidata"]))


(defroutes app-routes
  (GET "/:repository/type"
       [repository]
       (if-not (valid-repository repository)
         (not-found (response (:message "Repository Does Not Exist")))
         (response (dbpedia/get-static-types))))
  (GET "/:repository/properties/:type-label"
       [repository type-label]
       ;; TODO - validate repository in middleware
       (let [type-uri (dbpedia/get-type-uri-from-label type-label)]
         (cond
           (not (valid-repository repository))
           (not-found (response {:message "Repository Does Not Exist"}))
           (nil? type-uri)
           (not-found (response {:message "Type Does Not Exist"}))
           :else (response (dbpedia/get-properties-for-type-label
                            type-label
                            10)))))
  (not-found (response {:message "Not Found"})))


(def app
  (do
    (mount/start)
    (-> app-routes
        wrap-json-response
        wrap-json-body
        (wrap-defaults site-defaults))))
