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

    (defn rand-int-between [min max]
      (rand-nth (range min (inc max))))

    (defn shuffle-str [s]
      (str* (shuffle (into [] s))))

    (defn rand-alpha-str [min max]
      (str* (take (rand-int-between min max)
                  (repeatedly #(rand-nth (seq alphas))))))

    (defn rand-substring [s]
      (str* (take (rand-int (count s)) s)))

    (defn rand-shuffled-substring [s]
      (shuffle-str (rand-substring s)))

    (defn rand-shuffled-superstring [s]
      (shuffle-str (str s (rand-alpha-str 1 20))))

    (defn unused-alphas [s]
      (seq (apply disj alphas (set s))))

    (defn rand-non-substring [s]
      (when-let [others (unused-alphas s)]
        (let [s2 (take (inc (rand-int (dec (count others)))) others)]
          (shuffle-str (str* (rand-substring s) s2))))))

(def alpha-str
  (gen/fmap str*
            (gen/vector (gen/elements alphas))))

;; succeed on substrings
(def scramble-1
  (prop/for-all [v alpha-str]
                (scramble? v (rand-substring v))))

;; succeed on shuffled substrings
(def scramble-2
  (prop/for-all [v alpha-str]
                (scramble? v (rand-shuffled-substring v))))

;; fail on non-substrings
(def scramble-3
  (prop/for-all [v alpha-str]
                (if-let [v2 (rand-non-substring v)]
                  (not (scramble? v v2))
                  true)))

;; fail on superstrings
(def scramble-4
  (prop/for-all [v alpha-str]
                (not (scramble? v (rand-shuffled-superstring v)))))

(deftest scramble-gen-tests
  (ct/do-template
    [x]
    (is (:pass? (tc/quick-check 10000 x)))
    scramble-1
    scramble-2
    scramble-3
    scramble-4))