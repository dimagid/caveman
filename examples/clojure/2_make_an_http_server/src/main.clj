(ns main
  (:require [ring.adapter.jetty :as jetty]))

(defn handler
  [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello, world"})

(defn -main []
  (jetty/run-jetty #'handler {:port 9999}))