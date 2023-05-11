(ns canvas.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]
   [reagent.dom :as rdom]
   [canvas.data   :as d]))


(def two-pi (* 2 (.-PI js/Math)))

(defn draw-circle
  [{:keys [ctx x y r fill]}]
  (.beginPath ctx)
  (.arc ctx x y r 0 two-pi)
  (set! (.-fillStyle ctx) fill)
  (.fill ctx))


(defn draw-rectangle [{:keys [ctx x y width height fill]}]
  (set! (.-fillStyle ctx) fill)
  (.fillRect ctx x y width height))

(defn define-color [{:keys [ctx fill]}] (set! (.-fillStyle ctx) fill))

(defn canvas []
  (reagent/create-class
   {:component-did-mount
    (fn [comp]
      (let [node (rdom/dom-node comp)
            ctx (.getContext node "2d")]
        (.translate ctx 0 100)
        (draw-rectangle {:ctx ctx :x 100 :y 100 :width 230 :height 150 :fill "#005588"})
        (draw-circle    {:ctx ctx :x 20 :y 10 :r 10 :fill "#550088"})))
    :reagent-render
    (fn []
      [:canvas {:width 1500 :height 800 :style {:background-color "grey"}}])}))

(defn main-panel []
  [:div
   [canvas]])
(comment
  #_(def canvas (.getElementById js/document "main"))
  #_(set! (.-width canvas) "2000px")
  #_(set! (.-height canvas) "2000px")

  #_(def ctx (.getContext canvas "2d"))

  (.fillRect ctx 60 30 61 61)
  
  (def test-element (first d/data)))

;; Notes
;; Part 1 display the shapes
;; Todo, fix canvas display => use proper reagent with hooks, component did mount
;; type 3 component

;; Todo: how to display negative y or x values? 
;;      use ctx.translate, where too? => find out min x and y vals in data
;; -> x min: 619 x max 1376.5 y min: -78 y max: 331.5

;; Todo: Draw elements
;;      draw rectangle => .fillRect takes 4 args top left width height => rectangle
;; -- problems some rect are filled and some are not, but I cant 
;;    see how to tell in data
;;       Draw Cicle:
;;              function drawCircle() {
;;               ctx.beginPath()
;;               ctx.arc(circle.x, circle.y, circle.size, 0, pi)
;;               ctx.fillStyle = 'purple'
;;               ctx.fill() or ctx.stroke if empty
;;              }

;;       Make proper cljs fns and test them

;;       Make map fn to draw elements programatically


;; Part 2 figure how to draw an outline around the perimetter or a group of shapes
;;    event handlers only recover mouse possition, 
;;    there is no automatic way to identify each separate element or group
;;    also elements cannor hold data, so the groups need to be defined 
;;    somewhere else or someother way
;;   

;; Todo: figure out the perimeter of each group
;;     sort elements by group
;;     determine the the top left and bottom right corner of each group

;; Todo: Envent listener
;;     addEventListener("click", (event) => {});
;;    where does it go? is it in component did mount?

;; Todo: Write logic to dermine if the mouse is clicked over a group (cond ) 
         

;; Todo: update function that redraws the canvas with a strokeRect of the proper size over the right area
;; or just the components of clicked outside the bounds of a group


(comment
  #_{:shape-type :rect,
     :x 691,
     :y -48,
     :width 104,
     :height 4,
     :fill "#120000",
     :group 0}
  (set! (.-fillStyle ctx) "rgb(0,0,255)")
  (.beginPath ctx)
  (.rect ctx 24 30 61 61)
  (.fillRect ctx 691 -48 104 4)
  (.fill ctx)
  (.reset ctx)


  )

(comment

  (define-color "red")

  (.beginPath ctx)

  (.rect ctx 20 30 104 61)


  (defn draw-element [{:keys [shape-type x y width height radius fill]}]
    (js/console.log (str {:shape shape-type :x x :y y :widht width :h height :r radius :fill fill}))
    (define-color fill)
    (if
     (= shape-type :rect)
      (.rect ctx x y width height)

      (.arc ctx x y radius 0 two-pi))
    (.fill ctx)))
(comment
  (def e-test {:shape-type :rect,
               :x 20,
               :y 30,
               :width 10,
               :height 61,
               :fill "#1AAE9F", ;; teal
               :group 0})

  (map draw-element d/data)

  (draw-element e-test)



  (set! (.-fillStyle ctx) "grey")

  (-> ctx
      (.fillRect 7 6 104 61)))


