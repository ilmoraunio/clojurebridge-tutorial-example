(ns global-growth.test.core
  (:require [clojure.test :refer :all]
            [global-growth.core :refer :all]))

(deftest a-test
  (testing "Basic features"
    (is (= 1 1) "One must equal one.")
    (is (> 1 0) "One is higher than zero.")
    (are [x y] (= x y) 
         2 (+ 1 1)
         4 (* 2 2))
    (is (and (= true true)
             (= false false)
             (or false
                 true)))
    (is (every? #(= true %) (take 5 (repeat true))) "All entries in coll must equal to true.")
    (is (thrown? ArithmeticException (/ 1 0)) "Divide by zero returns exception.")
    (is (thrown-with-msg? ArithmeticException #"Divide by zero" (/ 1 0)) "...with error message.")
    (is (re-find #"foo" "foobar") "Now we have two problems.")))

(with-test
  (defn foo [x] x)
  (is (= "Hello world!" (foo "Hello world!"))))
;(meta #'foo)
;(meta #'a-test)

(defn a 
  [val]
  (is (= true val)))
(defn b [val] (a val))
(deftest c (b true))

(run-tests)