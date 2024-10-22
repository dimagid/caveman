(ns example.system
  (:require [ring.adapter.jetty :as jetty]
            [example.routes :as routes])
  (:import (org.eclipse.jetty.server Server)))

(defn start-server
  [system]
  (jetty/run-jetty
   (partial #'routes/root-handler system)
   {:port 9999
    :join? false}))

(defn stop-server
  [server]
  (Server/.stop server))

(defn start-system
  []
  (let [system-so-far {}]
    {::server (start-server system-so-far)}))

(defn stop-system
  [system]
  (stop-server (::server system)))