(ns challenge.server.scramble)

(defn char-occurence-table
  "turn a string into a map of type {char int}
   ex:
   (char-occurence-table \"aab\")
   ;=> {\\a 2 \\b 1}"
  [s]
  (reduce (fn [ret c]
            (if (contains? ret c)
              (update ret c inc)
              (assoc ret c 1)))
          {} (seq s)))

(defn consume-char
  "table :: {char int}
   c :: char
   if table contains occurences of c,
   return the same table with c occurence decremented
   else returns nil"
  [table c]
  (when (and (contains? table c)
             (pos? (get table c)))
    (update table c dec)))

(defn scramble?
  "impl 1"
  [x y]
  (reduce consume-char
          (char-occurence-table x) y))

(defn scramble?
  "impl 2"
  [x y]
  (loop [ret (char-occurence-table x) [fc & rc] y]
    (if (and ret fc)
      (recur (consume-char ret fc) rc)
      ret)))