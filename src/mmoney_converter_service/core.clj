(ns mmoney-converter-service.core
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [hiccup.middleware :refer [wrap-base-url]]
            [environ.core :refer [env]]
            [mmoney-converter.core :refer [read-configuration]]
            [mmoney-converter-service.middleware :refer [wrap-handlers]]
            [mmoney-converter-service.mmoney :as m]
            [mmoney-converter-service.view :as v]))

(def config (atom nil))

(defn init []
  (reset! config (read-configuration (env :mmoney-config "config.edn"))))

(defroutes routes
  (GET "/" [] (v/upload-page))
  (POST "/upload" {params :params}
    (let [result (m/convert-mmoney-xml @config params)]
      (if (= (:status result) :success)
        (v/handle-upload result)
        (v/handle-error result))))
  (GET "/download" {session :session}
    (v/handle-download session))
  ;;
  (route/resources "/")
  (route/not-found "Page not found"))

(def app (wrap-handlers #'routes))
