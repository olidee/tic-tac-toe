(ns tic-tac-toe.core-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe.core :refer :all]))

(deftest test-analyse
  (are [expected board] (= expected (analyse board))
    :x
    [:x :o :e
     :o :x :e
     :e :e :x]

    :o
    [:o :o :o
     :x :x :e
     :e :e :e]

    :ongoing
    [:x :e :e
     :e :x :e
     :o :o :e]

    :draw
    [:x :o :x
     :o :x :o
     :o :x :o]))

(deftest test-next-moves
  (are [board result] (= result (next-moves board :x))
    [:o :o :x
     :x :x :o
     :o :x :o]
    []

    [:o :o :x
     :e :e :e
     :o :x :o]

    [[:o :o :x
      :x :e :e
      :o :x :o]
     [:o :o :x
      :e :x :e
      :o :x :o]
     [:o :o :x
      :e :e :x
      :o :x :o]]))

(deftest test-game
  (are [board x y result] (= result (game board x y))
    [:o :o :e
     :e :e :x
     :x :x :e]
    3 1
    {:board [:o :o :o :e :e :x :x :x :e] :state :o}

    [:o :o :x
     :e :x :x
     :x :x :o]
    1 2
    {:board [:o :o :x :o :x :x :x :x :o] :state :draw})

  (let [board [:e :o :e
               :e :e :e
               :x :e :e]
        result (game board 1 1)]
    (is (= :ongoing (:state result))))

  (let [board [:e :x :e
               :o :e :o
               :e :x :e]]
    (is (thrown? AssertionError (game board 2 1)))
    (is (thrown? AssertionError (game board 1 2)))))
