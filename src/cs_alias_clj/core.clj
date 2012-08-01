(ns cs-alias-clj.core
  (:gen-class)
  (:use clojure.java.io)
  (:use clojure.math.numeric-tower))

(defn read-ff
  "Reads smth words from file to list"
  [filename]
  (let [rdr (reader filename)]
    (line-seq rdr)))

(defn write-ff
  "Write smth string to file"
  [filename string]
  (let [wrtr (writer filename)]
    (.write wrtr string)
    (.close wrtr)))

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
                                  (str result-str
                                       "p{"
                                       (float (/ (floor (*
                                                         (float (/ 1 cards-on-row))
                                                         10))
                                                 10))
                                       "\\linewidth"
                                       "}"
                                       "|")))))]
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
  "Create tables with word cards from file words.txt"
  [cards-on-col cards-on-row words-on-card]
  (make-item-cards (read-ff "words.txt")
                   cards-on-col cards-on-row words-on-card))

(defn make-people-cards
  "Create tables with people cards from file people.txt"
  [cards-on-col cards-on-row people-on-card]
  (make-item-cards (read-ff "people.txt")
                  cards-on-col cards-on-row people-on-card))

(defn make-tex-doc
  "Create LaTeX document"
  [cards-on-col cards-on-row]
  (defn preamble []
    (str "\\documentclass[a4paper, 12pt]{article}\n"
         "\\usepackage[T2A]{fontenc}\n"
         "\\usepackage{ucs}\n"
         "\\usepackage[utf8x]{inputenc}\n"
         "\\usepackage[english, russian]{babel}\n"
         "\\usepackage[left=0mm,right=0mm,"
         "top=0mm,bottom=0mm,bindingoffset=0cm]{geometry}\n"
         "\\parindent=0mm\n\n"
         "\\begin{document}\n"))
  (str
   (preamble)
   (make-word-cards cards-on-col cards-on-row 8)
   (make-people-cards cards-on-col cards-on-row 8)
   "\\end{document}\n"))

(defn -main []
  (do
    (print "Hello! Now I am reading words and people names from ")
    (print "files 'words.txt' and 'people.txt'. LaTeX source file ")
    (print "'alias.tex' containing cards for 'Alias' will be generated. ")
    (print "That file will contain the descriptions of 3x3 tables of ")
    (println "cards on every page.")
    (write-ff "alias.tex" (make-tex-doc 3 3))))
