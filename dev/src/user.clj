(ns user
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [mmoney-converter-service.core :refer [app init]]))

(def server (atom nil))

(defn run-server
  ([] (run-server 3000))
  ([port]
   (when-let [running-server @server]
     (println "Stopping server")
     (.stop running-server))
   (init)
   (reset! server (run-jetty app {:port port
                                  :join? false}))))
