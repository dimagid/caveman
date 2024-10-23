(ns example.cave.routes
  (:require [hiccup2.core :as hiccup]
            [example.middleware :as middleware]))

(defn cave-create-handler
  [_system _request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (str
             (hiccup/html
              [:html
               [:body
                [:h1 "TODO"]]]))})

(defn cave-handler
  [_system _request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (str
             (hiccup/html
              [:html
               [:body
                [:h1 "Create a new cave"]
                [:form {:method "post"
                        :action "/cave/create"}
                 [:label {:for "description"} "Description"]
                 [:input {:name "description" :type "text"}]
                 [:input {:type "submit"}]]]]))})

(defn routes
  [system]
  [""
   {:middleware (middleware/standard-html-route-middleware system)}
   ["/cave" {:get {:handler (partial #'cave-handler system)}}]
   ["/cave/create" {:post {:handler (partial #'cave-create-handler system)}}]])