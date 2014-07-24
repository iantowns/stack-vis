(ns quil-demo.core
  (:use quil.core))
;(defn split-on-width [width string]
;  (println "string:" string)
;  (println "string length: " (count string))
;  (println "split string: " (clojure.string/split (str string) #"\s+"))
;  (println "trial sequence: " (reduce (fn [x y] (if (< (+ (.length x) (.length y)) 5)  (str x "&" y) (str x "*" y)) ) (clojure.string/split (str string) #"\s+")))
;  (reduce (fn [x y] (if (< (+ (.length x) (.length y)) width) (let [printx (println "x: " x) printy (println "y: " y)](str x "&" y)) (let [printx (println "str y: " y) printy (println "str x: " x)] (str )))) (clojure.string/split (str string) #"\s+")))
 
;(defn split-on-width [width string]
;  "returns sequence of strings broken into chunks of length width"
;  (loop [split-strings (clojure.string/split (str string) #"\s+") w width]
;    (let [x (first split-strings)
;          y (first (rest split-strings))
;          z (rest (rest split-strings))
;          p-x (println "x" x)
;          p-y (println "y" y)
;          p-z (println "z" z)]
;      (if (< (+ (.length x) (.length y)) width) (if (not (empty? z)) (cons (str x "+" y) (recur z width)) z) ;probably shouldn't be z
;        (if (not (empty? z)) (cons x (recur (rest split-strings) width)) z)
;        ))))

(def int-stack '(1 2 3243 342 23 945 85 83 92 2 4 5))
(def sample  '( "the only way to play this is to lose immediately" "enqueued" "disinterested and disentangled" "undone"))
(defn simple-split [width string]
  "returns collection of strings broken into chunks of length determined by 'width'"
  (let [split-string (clojure.string/split (str string) #"\s+")]
    (reduce (fn [x-start y] (let [x (if (not (coll? x-start)) [x-start] x-start)
                                  printx (println "x is now" x)
                                  printy (println "y is now" y) ]
                              (conj x y))) split-string)))
;(println "simple split" (simple-split 30 sample))

(defn split-on-width [width string]
  "returns sequence of strings broken into chunks of length determined by 'width'"
  (let [split-string (clojure.string/split (str string) #"\s+")]
    (reduce (fn [x-start y] (let [ x (if (not (coll? x-start)) [x-start] x-start)
                                  print-x (println "x is " x)
                                  print-y (println "y is " y)
                                  xandy (println "x+y " [(str (first x) "+" y)]) 
                                  result (println "result "(conj (rest x) (str (first x) "+" y) )) ]
              (if (< (+ (.length (str (first x))) (.length (str y))) width) (conj (rest x) (str (first x) "+" y) ) (conj x y)))) split-string)))
    
;(println "split on width " (split-on-width 20 sample))
 
(defn draw-split [width y-pos string-coll x-pos] 
  (let [
        string-print (println "string coll: " string-coll)
        string-len  (count string-coll)
        width-rpint (println "width" width)
        len-print (println "string-len" string-len)] 
    (dotimes [n string-len]
      (text (str (nth string-coll n)) (+ x-pos 15) (- y-pos (* n 17) ) ))))

(defn stack-text [coll bt stk-wid x-pos] 
  (println "coll" coll)
  (loop[c (reverse coll) ht (- bt 8)]
          (println "method")
             (when (not (empty? c)) 
                 (let [width 10
                       split-str (split-on-width (/ stk-wid 8) (str (first c)))
                       split-string (if (not (coll? split-str)) [split-str] split-str) 
                       print-split (println "split string result" split-string)
                       num-splits (count split-string)
                       print-ht (println "print-ht " (* (- ht 20) num-splits))
                       ]
                 (do (draw-split width ht split-string x-pos)
                 (if (> num-splits 1) (recur (rest c) (- ht (* 20 num-splits)))  
                 (recur (rest c) (- ht 23)))))
               )
    )
 )

(defn setup []
  (smooth)
  (background 230 230 230)
  
  (def use-width 800)
  (def use-height 400)
  (def code-stack '((+ 1 2) (count ()) (str 245) (/ 2 4) (/ 2 (+ 4 (* 2 (/ 1 3))))))
  (def some-stacks (conj [code-stack] sample int-stack))
  (stroke-cap :square)
  (stroke-weight 4)
  (let [font            (create-font "Monaco" 12)
        txt            (text-font font)
        print-stacks (println "some stacks" some-stacks)
        number-of-stacks (count some-stacks)
        print-stack-count (println "stack count" (count some-stacks))
        canvas-x-center (/ use-width 2)
        canvas-y-center (/ use-height 2)
        bottom             (- use-height 10)
        right              (- use-width 10)
        print-right        (println right)
        stack-width        (/ (- use-width 20) number-of-stacks  )
        print-step         (println (/ (- use-width 20) (+ number-of-stacks 1) ))
        line-increments (range  10  ( + right ( / 4 2)) stack-width)]
    (fill 0 0 0)
    (dorun (map (fn [x] (line x 10 x bottom)) line-increments))
    (line (- 10 (/ 4 2) ) bottom ( + right (/ 4 2) ) bottom) 
    (dotimes [n (count some-stacks)]
      (let [print-stack (println "stack" (nth some-stacks n))
            print-code-stack (println "code stack" code-stack)]
      (stack-text (nth some-stacks n) bottom stack-width (* n stack-width)))
    )
    
    ))

(defsketch gen-art-1
  :title "Cross with circle"
  :setup setup
  :size [800 400])


