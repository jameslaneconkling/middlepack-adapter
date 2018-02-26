(defproject middlepack-adapter "0.1.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]

                 #_[org.eclipse.rdf4j/rdf4j-runtime "2.2.4"]
                 #_[org.eclipse.rdf4j/rdf4j-repository-sail "2.2.4"]
                 #_[org.eclipse.rdf4j/rdf4j-sail-memory "2.2.4"]

                 [grafter "0.8.12"]
                 [compojure "1.6.0"]
                 [ring/ring-defaults "0.2.1"]
                 [mount "0.1.11"]
                 [ring/ring-json "0.4.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler middlepack-adapter.handler/app
         :nrepl {:start? true
                 :port 9998}}
  :target-path "target/%s"
  :profiles {:dev {:source-paths ["src" "test" "dev"]
                   :dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.0"]]}})
