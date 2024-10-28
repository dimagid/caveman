(ns example.main
  (:require
   [example.system :as system]
   [ring.adapter.jetty :as jetty]))

(defn -main []
  (system/start-system))
