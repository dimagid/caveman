(ns example.cave.jobs
  (:require [example.system :as-alias system]
            [next.jdbc :as jdbc])
  (:import (java.util UUID)))

(set! *warn-on-reflection* true)

(defn process-cave-insert
  [{::system/keys [db]} _job-type payload]
  (jdbc/execute!
   db
   ["INSERT INTO prehistoric.hominid(name, cave_id)
      VALUES (?, ?)"
    "Grunk"
    (UUID/fromString (:id payload))]))

(defn handlers
  []
  {:prehistoric.cave/insert #'process-cave-insert})
