(ns cs-alias-clj.test.core
  (:use [cs-alias-clj.core])
  (:use [clojure.test]))

(deftest test-make-item-cell
  (is
   (= (make-item-cell '("a" "b" "c" "d") 3)
      (str "\\begin{enumerate}\n"
           "\\item a\n"
           "\\item b\n"
           "\\item c\n"
           "\\end{enumerate}"))
   "Test make-item-cell (items > items-on-card")
  (is
   (= (make-item-cell '("a" "b" "c" "d") 4)
      (str "\\begin{enumerate}\n"
           "\\item a\n"
           "\\item b\n"
           "\\item c\n"
           "\\item d\n"
           "\\end{enumerate}"))
   "Test make-item-cell (items = items-on-card")
  (is
   (= (make-item-cell '("a" "b" "c" "d") 5) "")
   "Test make-item-cell (items < items-on-card"))

(deftest test-make-item-string
  (is
   (= (make-item-string
       '("a" "b" "c" "d" "e" "f")
       3 2)
      (str "\\begin{enumerate}\n"
           "\\item a\n"
           "\\item b\n"
           "\\end{enumerate}"
           " & "
           "\\begin{enumerate}\n"
           "\\item c\n"
           "\\item d\n"
           "\\end{enumerate}"
           " & "
           "\\begin{enumerate}\n"
           "\\item e\n"
           "\\item f\n"
           "\\end{enumerate}"
           " \\\\"))
   "Test make-item-string (items = items-on-card * cards-on-row")
  (is
   (= (make-item-string
       '("a" "b" "c" "d" "e" "f" "g" "h")
       3 2)
      (str "\\begin{enumerate}\n"
           "\\item a\n"
           "\\item b\n"
           "\\end{enumerate}"
           " & "
           "\\begin{enumerate}\n"
           "\\item c\n"
           "\\item d\n"
           "\\end{enumerate}"
           " & "
           "\\begin{enumerate}\n"
           "\\item e\n"
           "\\item f\n"
           "\\end{enumerate}"
           " \\\\"))
   "Test make-item-string (items > items-on-card * cards-on-row")
  (is
   (= (make-item-string '("a" "b" "c") 3 2) "")
   "Test make-item-string (items < items-on-card * cards-on-row"))

(deftest test-make-item-table
  (is
   (= (make-item-table '("a" "b") 2 2 8) "")
   "Test make-item-table (items < needed items)")
  (is
   (= (make-item-table '("a") 1 1 1)
      (str "\\begin{tabular}{|l|}\n"
           "\\hline\n"
           "\\begin{enumerate}\n"
           "\\item a\n"
           "\\end{enumerate}"
           " \\\\"
           "\n"
           "\\hline\n\\end{tabular}"))
   "Test make-item-table (items = needed items)")
  (is
   (= (make-item-table '("a" "b" "c") 1 1 1)
      (str "\\begin{tabular}{|l|}\n"
           "\\hline\n"
           "\\begin{enumerate}\n"
           "\\item a\n"
           "\\end{enumerate}"
           " \\\\"
           "\n"
           "\\hline\n\\end{tabular}"))
   "Test make-item-table (items > needed items)"))

(deftest test-make-item-cards
  (is
   (= (make-item-cards '("a" "b") 2 2 8) "")
   "Test make-item-cards (items < needed items)")
  (is
   (= (make-item-cards '("a") 1 1 1)
      (str "\\begin{tabular}{|l|}\n"
           "\\hline\n"
           "\\begin{enumerate}\n"
           "\\item a\n"
           "\\end{enumerate}"
           " \\\\"
           "\n"
           "\\hline\n\\end{tabular}"
           "\n\\newpage\n"))
   "Test make-item-cards (items = needed items)")
  (is
   (= (make-item-cards '("a" "b" "c") 1 1 1)
      (str "\\begin{tabular}{|l|}\n"
           "\\hline\n"
           "\\begin{enumerate}\n"
           "\\item a\n"
           "\\end{enumerate}"
           " \\\\"
           "\n"
           "\\hline\n\\end{tabular}"
           "\n\\newpage\n"
           "\\begin{tabular}{|l|}\n"
           "\\hline\n"
           "\\begin{enumerate}\n"
           "\\item b\n"
           "\\end{enumerate}"
           " \\\\"
           "\n"
           "\\hline\n\\end{tabular}"
           "\n\\newpage\n"
           "\\begin{tabular}{|l|}\n"
           "\\hline\n"
           "\\begin{enumerate}\n"
           "\\item c\n"
           "\\end{enumerate}"
           " \\\\"
           "\n"
           "\\hline\n\\end{tabular}"
           "\n\\newpage\n"))
   "Test make-item-cards (items > needed items)"))
