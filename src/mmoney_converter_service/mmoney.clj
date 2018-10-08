(ns mmoney-converter-service.mmoney
  (:require [clojure.java.io :as io]
            [taoensso.timbre :as timbre]
            [mmoney-converter.core :as mc]
            [mmoney-converter.mmoney :as mm]
            [mmoney-converter.excel :as xls])
  (:import (java.io File ByteArrayOutputStream)))

(defn create-output-filename [filename]
  (let [[fname ext] (rest (re-matches #"^(.+)\.(.+)$" (or filename "")))]
    (if (and fname ext)
      (str fname ".xlsx")
      "result.xlsx")))

(defn convert-mmoney-xml [config params]
  (let [{:keys [filename content-type tempfile]} (:upload-file params)
        log-collector (mc/configure-collecting-logging!)]
    (try
      (if (contains? #{"application/xml" "text/xml"} content-type)
        (let [baos (ByteArrayOutputStream.)]
          (-> (.getAbsolutePath tempfile)
              (mm/parse-file)
              (xls/export config baos))
          {:status    :success
           :data      (.toByteArray baos)
           :file-name (create-output-filename filename)
           :log       @log-collector})
        (throw (ex-info "Invalid file type" {:content-type content-type})))
      (catch Exception ex
        (timbre/error ex)
        {:status   :error
         :log @log-collector}))))