(ns example.routes
  (:require
   [clojure.tools.logging :as log]
   [example.cave.routes :as cave-routes]
   [example.goodbye.routes :as goodbye-routes]
   [example.hello.routes :as hello-routes]
   [hiccup2.core :as hiccup]
   [reitit.ring :as reitit-ring]))

(defn routes
  [system]
  [""
   (cave-routes/routes system)
   (hello-routes/routes system)
   (goodbye-routes/routes system)])

(defn not-found-handler
  [_request]
  {:status 404
   :header {"Content-Type" "text/html"}
   :body (str
          (hiccup/html
           [:html
            [:body
             [:h1 "Not Found"]]]))})

;; Added a curried overload to root-handler
(defn root-handler
  ([system request]
   ((root-handler system) request))
  ([system]
   (let [handler (reitit-ring/ring-handler
                  (reitit-ring/router
                   (routes system))
                  #'not-found-handler)]
     (fn root-handler [request]
       (log/info (str (:request-method request) " - " (:uri request)))
       (handler request)))))
