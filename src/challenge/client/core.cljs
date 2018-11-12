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

(defn lower-alpha? [x]
  (re-matches #"[a-z]*" x))

(defn main []

  (let [inner-state
        (atom {:str1 "rekqodlw"
               :str2 "world"
               :scrumbled? true
               :warning nil})

        refresh
        (fn []
          (go (let [response
                    (<! (http/get
                          (str (.-href js/window.location) "scramble")
                          {:query-params
                           {"x" (:str1 @inner-state)
                            "y" (:str2 @inner-state)}}))]
                (swap! inner-state assoc
                       :scrumbled? (:body response)))))

        set-str-field
        (fn [k e]
          (swap! inner-state assoc k (tval e))
          (if (and (lower-alpha? (:str1 @inner-state))
                   (lower-alpha? (:str2 @inner-state)))
            (do (swap! inner-state dissoc :warning)
                (refresh))
            (swap! inner-state assoc :warning
                   "Lower-case alpha numeric chars only!")))]

    (fn []
      [:div

       [:div.title "Scrumble?"]

       [:div.description
        "Does a portion of str1 characters can be rearranged to match str2?"]

       [:input
        {:type "text"
         :value (:str1 @inner-state)
         :on-change #(set-str-field :str1 %)}]

       [:input
        {:type "text"
         :value (:str2 @inner-state)
         :on-change #(set-str-field :str2 %)
         :class
         (cond
           (:warning @inner-state) "warned"
           (:scrumbled? @inner-state) "valid"
           :else "invalid")}]

       [:div.warning (:warning @inner-state)]])))

(r/render-component [main]
                    (.getElementById js/document "app"))
