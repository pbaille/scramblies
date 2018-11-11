(ns challenge.server.core-test
  (:require [clojure.test :refer :all]
            [challenge.server.scramble :refer [scramble?]]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.template :as ct]))

;; unit ---------------------------------------------

(deftest scramble-true
  (ct/do-template
    [a b]
    (is (scramble? a b))
    "abc" "abc"
    "abc" "bca"
    "abc" "bc"
    "abc" ""
    "rekqodlw" "world"
    "cedewaraaossoqqyt" "codewars"))

(deftest scramble-fail
  (ct/do-template
    [a b]
    (is (not (scramble? a b)))
    "abc" "abcd"
    "abc" "bcaa"
    "abc" "xbc"
    "katas" "steak"))

;; generative ----------------------------------------

(def alphas (into #{} "abcdefghijklmnopqrstuvwxyz"))

(do :impl

    (def str* (partial apply str))

    (defn rand-substring [s]
      (let [c (count s)]
        (str* (take (rand-int c) s))))

    (defn rand-shuffled-substring [s]
      (str* (shuffle (into [] (rand-substring s)))))

    (defn unused-alphas [s]
      (seq (apply disj alphas (set s))))

    (defn rand-non-substring [s]
      (when-let [others (unused-alphas s)]
        (let [s2 (take (inc (rand-int (dec (count others)))) others)]
          (str* (shuffle (concat s s2)))))))

(def alpha-str
  (gen/fmap str*
            (gen/vector (gen/elements alphas))))

(def scramble-1
  (prop/for-all [v alpha-str]
                (scramble? v (rand-substring v))))

(def scramble-2
  (prop/for-all [v alpha-str]
                (scramble? v (rand-shuffled-substring v))))

(def scramble-3
  (prop/for-all [v alpha-str]
                (if-let [v2 (rand-non-substring v)]
                  (not (scramble? v v2))
                  true)))

(deftest scramble-gen-tests
  (ct/do-template
    [x]
    (is (:pass? (tc/quick-check 10000 x)))
    scramble-1
    scramble-2
    scramble-3))