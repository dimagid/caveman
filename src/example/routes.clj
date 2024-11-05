(ns example.routes
  (:require
   [clojure.tools.logging :as log]
   [example.cave.routes :as cave-routes]
   [example.goodbye.routes :as goodbye-routes]
   [example.hello.routes :as hello-routes]
   [example.static.routes :as static-routes]
   [hiccup2.core :as hiccup]
   [reitit.ring :as reitit-ring]))

;; Potential circular dependency: example.cave.routes and example.hello.routes depend on example.system, and example.system depends on example.routes, which in turn depends on these routes. This creates a circular dependency. Notably, only routes that interact with the database (currently example.hello.routes and example.cave.routes) use the :as-alias to refer to example.system, in an attempt to mitigate this circular dependency. To avoid this, consider reorganizing the code so that these routes do not directly depend on example.system, for example by creating a new namespace that contains common logic between these routes and example.system, and having both namespaces depend on this new namespace.

;; Reitit expects the first route to be the base route, so we use "" to indicate the root URL "/"
(defn routes
  [system]
  [""
   (static-routes/routes system)
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
  "Handles HTTP requests for the application.

  This function has two overloads:

  * One that takes a `system` and a `request` as arguments, which is used for development.
  * One that takes only a `system` as an argument, which is used for production.

  In production, the function returns a handler that can be used to handle multiple requests.
  In development, the function compiles the routes on every request.

  Args:
    system: The application system.
    request: The HTTP request.

  Returns:
    A response to the HTTP request."
  ([system request]
   ((root-handler system) request))
  ([system]
   (let [handler (reitit-ring/ring-handler
                  (reitit-ring/router
                   (routes system))
                  #'not-found-handler)]
     (fn root-handler [request]
       (let [request-info (format "Request: %s - %s - %s"
                                  (:request-method request)
                                  (:uri request)
                                  (:remote-addr request))]
         (log/debug request-info)
         (log/info request-info)
         (handler request))))))
