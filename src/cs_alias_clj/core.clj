(ns cs-alias-clj.core
  (:gen-class)
  (:import (java.io BufferedReader FileReader)))

(defn read-ff
  "Reads smth words from file to list"
  [filename]
  (line-seq (BufferedReader. (FileReader. filename))))

(defn make-item-cell
  "Create table cell from given items list"
  [item-list items-on-card]
  (cond
    (< (count item-list) items-on-card) ""
    :else
    (str "\\begin{enumerate}\n"
         (loop [il (cond
                    (> (count item-list) items-on-card)
                    (take items-on-card item-list)
                    :else item-list)
                result-str ""]
           (cond
            (= (count il) 0) result-str
            :else (recur
                   (next il)
                   (str result-str
                        "\\item "
                        (first il)
                        "\n"))))
         "\\end{enumerate}")))

(defn make-item-string
  "Create table string from given items list"
  [item-list cards-on-row items-on-card]
  (defn count-of-items []
    (* cards-on-row items-on-card))
  (cond
   (< (count item-list) (count-of-items)) ""
   :else
   (loop [il (cond
              (> (count item-list)
                 (count-of-items))
              (take (count-of-items) item-list)
              :else item-list)
          result-str ""]
     (cond
      (= (count il) items-on-card)
      (str result-str
           (make-item-cell il items-on-card)
           " \\\\")
      :else (recur
             (nthnext il items-on-card)
             (str result-str
                  (make-item-cell (take items-on-card il) items-on-card)
                  " & "))))))

(defn make-item-table
  "Create table from given item list"
  [item-list cards-on-col cards-on-row items-on-card]
  (defn count-of-items []
    (* cards-on-col cards-on-row items-on-card))
  (defn count-of-items4string []
    (* cards-on-row items-on-card))
  (cond
   (< (count item-list) (count-of-items)) ""
   :else
   (loop
       [il (cond
            (> (count item-list) (count-of-items))
            (take (count-of-items) item-list)
            :else item-list)
        result-str (str "\\begin{tabular}"
                        (loop
                            [counter cards-on-row
                             result-str "{|"]
                          (cond
                           (= counter 0) (str result-str "}\n")
                           :else (recur
                                  (- counter 1)
                                  (str result-str "l|")))))]
     (cond
      (= (count il) 0) (str result-str "\\hline\n\\end{tabular}")
      :else (recur (nthnext il (count-of-items4string))
                  (str result-str "\\hline\n"
                       (make-item-string
                        (take (count-of-items4string) il)
                        cards-on-row
                        items-on-card) "\n"))))))

(defn make-item-cards
  "Create a set of item cards from given word-list"
  [item-list cards-on-col cards-on-row items-on-card]
  (defn count-of-items []
    (* cards-on-col cards-on-row items-on-card))
  (cond
   (< (count item-list) (count-of-items)) ""
   :else
   (loop [il item-list
          result-str ""]
     (cond
      (< (count il) (count-of-items)) result-str
      :else (recur
             (nthnext il (count-of-items))
             (str result-str
                  (make-item-table
                   (take (count-of-items) il)
                   cards-on-col
                   cards-on-row
                   items-on-card)
                  "\n\\newpage\n"))))))

(defn make-word-cards
  ""
  [cards-on-col cards-on-row words-on-card]
  (make-item-cards (read-ff "words.txt")
                   cards-on-col cards-on-row words-on-card))

(defn make-people-cards
  ""
  [cards-on-col cards-on-row people-on-card]
  (make-item-cell (read-ff "people.txt")
                  cards-on-col cards-on-row people-on-card))
