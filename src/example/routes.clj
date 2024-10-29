(ns example.routes
  (:require
   [clojure.tools.logging :as log]
   [hiccup2.core :as hiccup]
   [reitit.ring :as reitit-ring]))

(defn hello-handler
  [_system _request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str
          (hiccup/html
           [:html
            [:body
             [:h1 "Hello, world"]]]))})

(defn goodbye-handler
  [_system _request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str
          (hiccup/html
           [:html
            [:body
             [:h1 "Goodbye, world"]]]))})

(defn routes
  [system]
  [["/" {:get {:handler (partial #'hello-handler system)}}]
   ["/goodbye" {:get {:handler (partial #'goodbye-handler system)}}]])

(defn not-found-handler
  [_request]
  {:status 404
   :header {"Content-Type" "text/html"}
   :body (str
          (hiccup/html
           [:html
            [:body
             [:h1 "Not Found"]]]))})

(defn root-handler
  [system request]
  (log/info (str (:request-method request) " - " (:uri request)))
  (let [handler (reitit-ring/ring-handler
                 (reitit-ring/router
                  (routes system))
                 #'not-found-handler)]
    (handler request)))
