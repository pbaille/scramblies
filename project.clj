(defproject challenge "0.1.0-SNAPSHOT"

  :description "Flexiana clojure challenge"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.439"]
                 [org.clojure/core.async "0.4.474"]
                 [org.clojure/tools.cli "0.4.1"]
                 [figwheel-sidecar "0.5.17"]
                 [reagent "0.8.1"]
                 [cljs-http "0.1.45"]
                 [garden "1.3.6"]
                 [ring "1.7.1"]
                 [compojure "1.6.0-beta1"]
                 [hiccup "1.0.5"]
                 [org.clojure/test.check "0.10.0-alpha3"]]

  :source-paths ["src" "script"]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-garden "0.3.0"]
            [lein-ring "0.12.1"]]

  :main challenge.server.core
  :aot [challenge.server.core]

  :ring
  {:handler challenge.server.core/app}

  :cljsbuild
  {:builds [{:id "min"
             :source-paths ["src"]
             :compiler {:main challenge.client.core
                        :asset-path "js/out"
                        :optimizations :advanced
                        :output-to "resources/public/js/out/main.min.js"
                        :output-dir "resources/public/js/out"}}]}

  :garden
  {:builds [{:id "screen"
             :source-paths ["src"]
             :stylesheet challenge.styles.core/screen
             :compiler {:output-to "resources/public/screen.css"
                        :pretty-print? false}}]}

  :aliases
  {"build"
   ["do"
    "clean"
    ["cljsbuild" "once"]
    ["garden" "once"]
    "uberjar"]})
