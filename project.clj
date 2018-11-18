(defproject mmoney-converter-service "0.1.0"
  :description "Simple web frontend to the mmoney-converter"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring/ring-core "1.7.0"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-jetty-adapter "1.7.0"]
                 [compojure "1.6.1"]
                 [prone/prone "1.6.0"]
                 [hiccup "1.0.5"]
                 [environ "1.1.0"]
                 ;;
                 [mmoney-converter "0.3.0"]]
  :ring {:handler mmoney-converter-service.core/app
         :init mmoney-converter-service.core/init
         :uberwar-name "mmoney-converter-service.war"}
  :profiles {:dev {:dependencies [[ring/ring-devel "1.7.0"]]
                   :plugins [[lein-ring "0.12.4"]
                             [lein-environ "1.1.0"]]
                   :source-paths ["env/dev" "dev/src"]
                   :resource-paths ["dev/resources"]
                   :env {:mmoney-config "config.edn"}}
             :uberjar {:source-paths ["env/prod"]}})
