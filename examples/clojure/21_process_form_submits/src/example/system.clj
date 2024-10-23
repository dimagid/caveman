(ns example.system
  (:require [example.routes :as routes]
            [next.jdbc.connection :as connection]
            [ring.adapter.jetty :as jetty])
  (:import (com.zaxxer.hikari HikariDataSource)
           (io.github.cdimascio.dotenv Dotenv)
           (org.eclipse.jetty.server Server)))

(set! *warn-on-reflection* true)

(defn start-env
  []
  (Dotenv/load))

(defn start-db
  [{::keys [env]}]
  (connection/->pool HikariDataSource
                     {:dbtype "postgres"
                      :dbname "postgres"
                      :username (Dotenv/.get env "POSTGRES_USERNAME")
                      :password (Dotenv/.get env "POSTGRES_PASSWORD")}))

(defn stop-db
  [db]
  (HikariDataSource/.close db))

(defn start-server
  [{::keys [env] :as system}]
  (jetty/run-jetty
   (partial #'routes/root-handler system)
   {:port  (parse-long (Dotenv/.get env "PORT"))
    :join? false}))

(defn stop-server
  [server]
  (Server/.stop server))

(defn start-system
  []
  (let [system-so-far {::env (start-env)}
        system-so-far (merge system-so-far {::db (start-db system-so-far)})]
    (merge system-so-far {::server (start-server system-so-far)})))

(defn stop-system
  [system]
  (stop-db (::db system))
  (stop-server (::server system)))