(ns challenge.styles.core
  (:require [garden.def :refer [defstylesheet defstyles]]
    [garden.units :refer [px]]))

;; Change defstylesheet to defstyles.
(defstyles screen
           [:body
            {:font-family "sans-serif"
             :padding (px 20)
             :color "grey"}
            [:.title
             {:font-size (px 25)
              :color "grey"
              :margin (px 10)
              :margin-bottom (px 20)
              }]
            [:.description
             {:margin (px 10)}]
            [:input
             {:margin (px 10)
              :padding (px 5)
              :font-size (px 20)
              :border "2px solid lightgrey"
              :outline "none"
              :border-radius (px 5)
              :color "grey"}
             [:&.valid {:background-color "#BCED91"}]
             [:&.invalid {:background-color "#ffa382"}]]])
