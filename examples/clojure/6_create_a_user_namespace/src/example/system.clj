(ns example.system
  (:require [ring.adapter.jetty :as jetty]
            [example.routes :as routes])
  (:import (org.eclipse.jetty.server Server)))

(defn start-server
  []
  (jetty/run-jetty #'routes/handler {:port 9999
                                     :join? false}))

(defn stop-server
  [server]
  (Server/.stop server))

(defn start-system
  []
  {::server (start-server)})

(defn stop-system
  [system]
  (stop-server (::server system)))