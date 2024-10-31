(ns user
  (:require
   [clojure.pprint :as pp]
   [example.system :as system]
   [honey.sql :as sql]
   [next.jdbc :as jdbc]))

(def system nil)

(defn start-system!
  []
  (if system
    (println "Already Started")
    (alter-var-root #'system (constantly (system/start-system)))))

(defn stop-system!
  []
  (when system
    (system/stop-system system)
    (alter-var-root #'system (constantly nil))))

(defn restart-system!
  []
  (stop-system!)
  (start-system!))

(defn server
  []
  (::system/server system))

(defn db
  []
  (::system/db system))

(defn env
  []
  (::system/env system))

(defn get-cave-table
  []
  (jdbc/execute! (db) ["SELECT * FROM prehistoric.cave"]))

(defn get-cave-table-honey
  []
  (jdbc/execute! (db)
                 (sql/format {:select [:*]
                              :from [:prehistoric.cave]})))

(defn get-cave-descriptions
  []
  (jdbc/execute! (db) ["SELECT description FROM prehistoric.cave"]))

(defn get-cave-descriptions-honey
  []
  (jdbc/execute! (db)
                 (sql/format {:select [:description]
                              :from [:prehistoric.cave]})))

(defn get-cave-descriptions-only
  []
  (map :cave/description (get-cave-descriptions)))

(defn get-hominids-table
  []
  (jdbc/execute! (db) ["SELECT * FROM prehistoric.hominid"]))

(comment

  *ns*

  system

  (start-system!)

  (stop-system!)

  (restart-system!)

  (get-cave-descriptions)

  (get-cave-descriptions-honey)

  (get-cave-descriptions-only)

  (pp/pprint (get-hominids-table))

  (pp/print-table (get-cave-table))

  (pp/print-table (get-cave-table-honey))

  #_())
