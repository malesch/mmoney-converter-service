(ns mmoney-converter-service.view
  (:use [hiccup core page])
  (:require [clojure.string :as string]
            [ring.util.response :as resp]
            [mmoney-converter-service.version :refer [version]])
  (:import (java.io ByteArrayInputStream)))

(defn base [content]
  (html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]
     (include-css "/css/bootstrap.min.css")
     (include-css "/css/styles.css")]
    [:body
     [:div.container
      content]
     ;;
     (include-js "/js/jquery-1.12.4.min.js")
     (include-js "/js/bootstrap.min.js")
     (include-js "/js/upload.js")]))

(defn upload-page [config]
  (base
    [:div
     [:div.row
      [:div.col-xs-12.col-md-6.col-md-offset-3.col-sm-8.col-sm-offset-2
       [:h1 "mMoney Converter"]]]
     ;
     [:div.row
      [:div.col-xs-12.col-md-6.col-md-offset-3.col-sm-8.col-sm-offset-2
       [:form {:action "/upload" :method "post" :enctype "multipart/form-data"}
        [:div.form-group
         [:div.input-group
          [:input#file-input-name.form-control {:type "text" :disabled "disabled"}]
          [:span.input-group-btn
           [:div.btn.btn-default.file-input
            [:span.glyphicon.glyphicon-folder-open {:aria-hidden "true"}]
            [:span.file-input-title "Browse"]
            [:input#fileUpload.form-control {:type "file" :name "upload-file"
                                             :accept "application/xml, text/xml"}]]]]]
        [:div.form-group
         [:button.btn.btn-primary "Convert"]]]]]
     [:div.row.version
      [:div.col-xs-12.col-md-6.col-md-offset-3.col-sm-8.col-sm-offset-2
       [:div.pull-right
        [:div {:title (str "Service version: " version)} (str "S: " version)]
        (when-let [cfg-version (:version config)]
          [:div {:title (str "Configuration version: " cfg-version)} (str "C: " cfg-version)])]]]]))

(defn display-messages [logs]
  [:div.console
   (for [{:keys [level message err]} logs
         :let [out (if (string/blank? message)
                     (or (.getMessage err) "Internal error")
                     message)]]
     [:div.line [:span {:class level} (h out)]])])

(defn handle-upload [{:keys [data file-name log]}]
  (-> (base
        [:div
         [:div.row
          [:div.col-xs-12.col-md-6.col-md-offset-3.col-sm-8.col-sm-offset-2
           [:h4 "The Excel document is ready for download!"]]]
         [:div.row
          [:div.col-xs-12.col-md-6.col-md-offset-3.col-sm-8.col-sm-offset-2
           [:a {:href "/download" :download file-name :target "_blank"}
            [:button#download.btn.btn-success.btn-block {:onclick "redirect_home()"} "Download"]]]]

         (when (pos? (count log))
           [:div.row
            [:div.col-xs-12.col-md-6.col-md-offset-3.col-sm-8.col-sm-offset-2
             (display-messages log)]])])
      resp/response
      (resp/content-type "text/html")
      (resp/charset "UTF-8")
      (assoc :session {:data data})))

(defn handle-error [{:keys [log]}]
  (base
    [:div
     [:div.row
      [:div.col-xs-12.col-md-6.col-md-offset-3.col-sm-8.col-sm-offset-2
       [:h3 "Something went wrong!"]
       [:a {:href "/"} "Retry"]]]

     (when (pos? (count log))
       [:div.row
        [:div.col-xs-12.col-md-6.col-md-offset-3.col-sm-8.col-sm-offset-2
         (display-messages log)]])]))

(defn handle-download [{:keys [data]}]
  (if data
    (-> (resp/response (ByteArrayInputStream. data))
        (assoc :session nil))
    (-> (base
          [:div
           [:div.row
            [:div.col-xs-12.col-md-6.col-md-offset-3.col-sm-8.col-sm-offset-2
             [:h3 "Download file not available"]]]])
        resp/response
        (resp/content-type "text/html")
        (resp/charset "UTF-8")
        (resp/status 400))))