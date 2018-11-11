(ns challenge.server.core
  (:require
    [compojure.core :refer :all]
    [compojure.route :as route]
    [ring.adapter.jetty :refer [run-jetty]]
    [ring.util.response :refer [content-type response]]
    [ring.middleware.params :refer [wrap-params]]
    [challenge.server.scramble :refer [scramble?]]
    [challenge.server.views :as views]
    [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(defroutes router
           (GET "/" [] (views/index))
           (GET "/scramble" [x y]
             (-> (response (str (scramble? x y)))
                 (content-type "application/edn")))
           (route/resources "/")
           (route/not-found "<h1>Page not found</h1>"))

(def app (wrap-params router))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]])

(defn -main [& args]
  (let [port (get-in (parse-opts args cli-options) [:options :port] 3456)]
    (run-jetty app {:port port})))