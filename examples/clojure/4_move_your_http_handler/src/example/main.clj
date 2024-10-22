(ns example.main
  (:require [ring.adapter.jetty :as jetty]
            [example.routes :as routes]))

(defn -main []
  (jetty/run-jetty #'routes/handler {:port 9999}))