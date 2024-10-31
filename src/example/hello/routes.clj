(ns example.hello.routes
  (:require
   [example.system :as-alias system]
   [hiccup2.core :as hiccup]
   [next.jdbc :as jdbc]))

(defn hello-handler
  [{::system/keys [db]} _request]
  (let [{:keys [planet]} (jdbc/execute-one!
                          db
                          ["SELECT 'earth' as planet"])]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (str
            (hiccup/html
             [:html
              [:body
               [:h1 "Hello, " planet]]]))}))

(defn routes
  [system]
  [["/" {:get {:handler (partial #'hello-handler system)}}]])
