(ns challenge.client.core
  (:require-macros
    [cljs.core.async.macros :refer [go]]
    [reagent.ratom :refer [reaction]])
  (:require
    [reagent.core :as r :refer [atom]]
    [cljs-http.client :as http]
    [cljs.core.async :refer [<!]]))

(enable-console-print!)

(defn log [& xs]
  (mapv #(js/console.log %) xs))

(defn tval [e]
  (.-value (.-target e)))

(defn main []

  (let [inner-state
        (atom {:str1 "rekqodlw"
               :str2 "world"
               :scrumbled? true})

        refresh
        (fn []
          (go (let [response
                    (<! (http/get
                          (str (.-href js/window.location) "scramble")
                          {:with-credentials? false
                           :query-params {"x" (:str1 @inner-state)
                                          "y" (:str2 @inner-state)}}))]
                #_(println response)
                (swap! inner-state assoc
                       :scrumbled? (:body response)))))]

    (fn []
      [:div

       [:div.title "Scrumble?"]

       [:div.description
        "Does a portion of str1 characters can be rearranged to match str2?"]

       [:input
        {:type "text"
         :value (:str1 @inner-state)
         :on-change
         (fn [e]
           (swap! inner-state assoc :str1 (tval e))
           (refresh))}]

       [:input
        {:type "text"
         :value (:str2 @inner-state)
         :class (if (:scrumbled? @inner-state) "valid" "invalid")
         :on-change
         (fn [e]
           (swap! inner-state assoc :str2 (tval e))
           (refresh))}]])))

(r/render-component [main]
                    (.getElementById js/document "app"))
