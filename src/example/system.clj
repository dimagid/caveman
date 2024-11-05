(ns example.system
  (:require
   [clojure.java.browse :as browse]
   [example.jobs :as jobs]
   [example.routes :as routes]
   [next.jdbc.connection :as connection]
   [proletarian.worker :as worker]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.session.cookie :as session-cookie])
  (:import
   (com.zaxxer.hikari HikariDataSource)
   (io.github.cdimascio.dotenv Dotenv)
   (org.eclipse.jetty.server Server)))

(set! *warn-on-reflection* true)

(defn start-env
  "Loads environment variables from a .env file, configuring the system for execution."
  []
  (Dotenv/load))

(defn start-cookie-store
  "Initializes a cookie store for HTTP sessions, enabling session management."
  []
  (session-cookie/cookie-store))

(defn start-db
  "Establishes a database connection using the provided `env` map, making the database available for queries."
  [{::keys [env]}]
  (connection/->pool HikariDataSource
                     {:dbtype "postgres"
                      :dbname "postgres"
                      :username (Dotenv/.get env "POSTGRES_USERNAME")
                      :password (Dotenv/.get env "POSTGRES_PASSWORD")}))

(defn stop-db
  "Closes the database `db` connection, releasing system resources."
  [db]
  (HikariDataSource/.close db))

(defn start-worker
  "Starts a worker process that consumes tasks from the database `db`, executing business logic."
  [{::keys [db] :as system}]
  (let [worker (worker/create-queue-worker
                db
                (partial #'jobs/process-job system)
                {:proletarian/log #'jobs/logger
                 :proletarian/serializer jobs/json-serializer})]
    (worker/start! worker)
    worker))

(defn stop-worker
  "Terminates the `worker` process, halting task execution."
  [worker]
  (worker/stop! worker))

(defn start-server
  "Starts a web server, making the system available for incoming requests."
  [{::keys [env] :as system}]
  (let [handler (if (= (Dotenv/.get env "ENVIRONMENT") "development")
                  (partial #'routes/root-handler system)
                  (routes/root-handler system))
        port (parse-long (Dotenv/.get env "PORT"))]
    (println (str "Web server started on port " port " on host localhost - " "\033[94mhttp://localhost:" port "\033[0m"))
    (browse/browse-url (str "http://localhost:" port)) ; open the default web browser
    (jetty/run-jetty
     handler
     {:port port
      :join? false})))

(defn stop-server
  "Stops the web `server`, preventing further incoming requests."
  [server]
  (Server/.stop server))

(defn start-system
  "Initializes the system, including environment variables, cookie store, database connection, worker, and web server, making it ready for execution."
  []
  (-> {::env (start-env)}
      (#(merge % {::cookie-store (start-cookie-store)}))
      (#(merge % {::db (start-db %)}))
      (#(merge % {::worker (start-worker %)}))
      (#(merge % {::server (start-server %)}))))

(defn stop-system
  "Shuts down the `system`, including the web server, worker, and database connection, releasing all system resources."
  [system]
  (stop-server (::server system))
  (stop-worker (::worker system))
  (stop-db (::db system)))
