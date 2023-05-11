(ns canvas.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]
   [reagent.dom :as rdom]
   [canvas.data   :as d]))


(def two-pi (* 2 (.-PI js/Math)))

(defn draw-circle
  [{:keys [ctx x y radius fill]}]
  (.beginPath ctx)
  (.arc ctx x y radius 0 two-pi)
  (set! (.-fillStyle ctx) fill)
  (.fill ctx))


(defn draw-rectangle 
  [{:keys [ctx x y width height fill]}]
  (set! (.-fillStyle ctx) fill)
  (.fillRect ctx x y width height))

(defn group-border
  [{:keys [ctx x y width height]}]
  (set! (.-fillStyle ctx) "purple")
  (.strokeRect ctx x y width height))

(defn draw-element [ctx {:keys [  shape-type x y width height radius fill]}]  
  (println { :shape-type shape-type :radius radius :ctx ctx :x x :y y :width width :height height :fill fill})
  (cond
    (= shape-type :rect)
    (draw-rectangle {:ctx ctx :x x :y y :width width :height height :fill fill})
    (= shape-type :circle)
    (draw-circle    {:ctx ctx :x x :y y :radius radius :fill fill})
    :else
    (js/console.log "condition is not shit")))

(defn draw-elements [ctx]
  (map (partial draw-element ctx) d/data))


(defn outline-group [e ctx]
 (let [x (.-x e)
       y (.-y e)] 
   (js/console.log (str "x " x " y " y))
   (cond
       (and (> y 63) (< y 143) (> x 1162) (< x 1277))
       (group-border {:ctx ctx :x 1140 :y -61 :width 134 :height 101})
       (and (> y 150) (< y 270) (> x 163) (< x 277))
       (group-border {:ctx ctx :x 140 :y 30 :width 134 :height 140})
       (and (> y 302) (< y 523) (> x 452) (< x 672))
       (group-border {:ctx ctx :x 430 :y 180 :width 240 :height 240})
       :else
       (do
         (.clearRect ctx 0 0 1500 800)
         (draw-rectangle  {:ctx ctx
                           :shape-type :rect,
                           :x 1150,
                           :y -51,
                           :width 114,
                           :height 81,
                           :fill "#C70A9D"})
         (draw-rectangle  {:ctx ctx
                           :shape-type :rect,
                           :x 150,
                           :y 40,
                           :width 114,
                           :height 120,
                           :fill "#C70A9D"})
         (draw-circle  {:ctx ctx
                        :shape-type :rect,
                        :x 550,
                        :y 300,
                        :radius 110,
                        :fill "#C70A9D"})))))


(comment 
  (draw-elements "jahoo")
  (map (partial draw-element "ctx") d/data)
  ;; For some reason when I try to do a map inside the :component-did-mount,
  ;; it just will not work. In the react dev tools there is an error that I cant interpret,
  ;; but my understanding is that something is timing out
(set! js/cdat (clj->js d/data))
  

  )

(defn canvas []
  (reagent/create-class
   {:component-did-mount
    (fn [comp]
      (let [node (rdom/dom-node comp)
            ctx (.getContext node "2d")]
        (.translate ctx 0 100)
        (draw-elements ctx)
        (.addEventListener node "click" (fn [event] (outline-group event ctx)))
        ;(map (fn [el] (draw-element (assoc el :ctx ctx ))) d/data) ;; it is not excecuted, but why?
        ;(map #(js/console.log %)  ["one" "two" "three"]) 
        ( draw-rectangle  {:ctx ctx
                           :shape-type :rect,
                           :x 1150,
                           :y -51,
                           :width 114,
                           :height 81,
                           :fill "#C70A9D",
                           })
        ;; bounds are top -54 left 1150 bottom 30 right 1264
        ( draw-rectangle  {:ctx ctx
                           :shape-type :rect,
                           :x 150,
                           :y 40,
                           :width 114,
                           :height 120,
                           :fill "#C70A9D",
                           })
        ;; bounds aez top 40 left 150 bottom 160 right 264
        ( draw-circle  {:ctx ctx
                        :shape-type :rect,
                        :x 550,
                        :y 300,
                        :radius 110,
                        :fill "#C70A9D"})
        ;; bounds are top 440 left 190 bottom 660 right 410
        ))
    :reagent-render
    (fn []
      [:canvas {:id "c" :width 1500 :height 800 :style {:border "5px solid grey"}}])}))

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

;;       Make proper cljs fns and test them  OK

;;       Make map fn to draw elements programatically
     ;;  I can't figure out why I cannot iterate in the 
  


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


