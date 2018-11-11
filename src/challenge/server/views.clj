(ns challenge.server.views
  (:use [hiccup core page]))

(defn index []
  (html5
    [:head
     [:title "Flexiana"]
     (include-css "/screen.css")]
    [:div#app]
    (include-js "/js/out/main.min.js")))