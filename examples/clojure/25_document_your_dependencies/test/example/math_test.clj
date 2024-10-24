(ns example.math-test
  (:require [clojure.test :as t]))

(t/deftest one-plus-one
  (t/is (= (+ 1 1) 2) "One plus one equals 2!"))
