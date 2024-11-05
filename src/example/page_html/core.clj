(ns example.page-html.core)

;; This namespace provides a reusable HTML template for generating web pages.

;; This function is designed to be reusable across multiple web pages in the application.
(defn view
  "Renders a basic HTML page with a customizable title and body content."
  [& {:keys [body title]
      :or {title "The Website"}}]
  [:html
   [:head
    [:title title]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]]
   [:body
    body]])
