(defproject middlepack-adapter "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]

                 #_[org.eclipse.rdf4j/rdf4j-runtime "2.2.4"]
                 #_[org.eclipse.rdf4j/rdf4j-repository-sail "2.2.4"]
                 #_[org.eclipse.rdf4j/rdf4j-sail-memory "2.2.4"]

                 [grafter "0.8.12"]
                 [mount "0.1.11"]]
  :main ^:skip-aot middlepack-adapter.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:source-paths ["src" "test" "dev"]}})
