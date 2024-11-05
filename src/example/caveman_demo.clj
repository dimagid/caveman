(ns example.caveman-demo
  (:gen-class)
  (:require
   [babashka.fs :as fs]
   [example.system :as system]))

(defn get-project-name []
  (fs/file-name (fs/cwd)))

(defn -main []
  (println (str "Loading application " (get-project-name) "..."))
  (system/start-system))
