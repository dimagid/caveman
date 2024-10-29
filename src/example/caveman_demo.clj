(ns example.caveman-demo
  (:gen-class)
  (:require
   [example.system :as system]))

(defn -main []
  (println "Starting the example web server on port 9999...")
  (system/start-system))
