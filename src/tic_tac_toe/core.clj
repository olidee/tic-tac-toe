(ns tic-tac-toe.core
  (:gen-class))

(def board-width 3)

(def max-index (* board-width board-width))

(def winning-sets
  [#{0 1 2} #{3 4 5} #{6 7 8} ; horizontal
   #{0 3 6} #{1 4 7} #{2 5 8} ; vertical
   #{0 4 8} #{2 4 6}])       ; diagonal

; could use medley/map-vals here, but since it's a test i'll do it the old fashioned way :)
(defn- flatten-board
  [[k squares]]
  [k (mapv first squares)])

(defn- board->map
  [b]
  (->> b
       (map-indexed #(conj [] %1 %2))
       (group-by second)
       (map flatten-board)
       (into {})))

(defn- winner?
  [squares]
  (->> winning-sets
       (some #(clojure.set/subset? % (into #{} squares)))))

(defn analyse
  [board]
  (let [{:keys [e x o]} (board->map board)]
    (cond
      (winner? x) :x
      (winner? o) :o
      (empty? e) :draw
      :else :ongoing)))

(defn next-moves
  [board mark]
  (let [{:keys [e]} (board->map board)]
    (map #(assoc board % mark) e)))

(defn- xy->idx
  [x y]
  (+ (dec x) (* board-width (dec y))))

(defn- make-move
  [board idx mark]
  {:pre [(#{:x :o} mark)]}
  (let [_ (assert (= :e (nth board idx)) "Illegal move")
        new-board (assoc board idx mark)]
    {:board new-board :state (analyse new-board)}))

(defn game
  [board x y]
  (let [idx (xy->idx x y)
        {:keys [board state] :as result} (make-move board idx :o)]
    (if (= :ongoing state)
      (make-move board (rand-int max-index) :x)
      result)))
