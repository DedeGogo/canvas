(ns canvas.views
  (:require
   [re-frame.core :as rf]
   [canvas.data   :as d]))

(def pi (.-PI js/Math ))

(def test-element (first d/data))

;; Use (-> ctx 
;;         (.method args))
;; Canvas methods :
;; .fillRect takes 4 args top left width height => rectangle
;; -- problems some rect are filled and some are not, but I cant 
;; -- see how to tell in data
;; .arc takes 4 args center-top center-left radius 
;; start-angle end-angle in radians (0 and 2π for a full circle)
;; ---how to tell if it is full or just the border?
;; ctx.arc(100, 75, 50, 0, 2 * Math.PI);
;; .fillStyle takes one arg a color
;; this gives me all but the group. I will put it either in the 
;; :key (str (:group) "-"(get-index)) => :key 0-23, or
;; put all elements in app-db and subscribe to the group

;; If I run (map draw-element d/data) it should draw all the elements,
;; the repl returns nil, as it should.
;; but nothing is rendered. 
;; I think the problem is that for some reason I am supper zoomed in 
;; on the canvas, but I dont know why and I can't find any info.
;; the border of the images are very blury, which supports the idea 
;; of being zoomed in, as does the fact that the pixel values are not rendred
;; in "view port" pixels but un some thing larger.

;; I have spent most of my time with this bug. But I just saw the time, and
;; it has been over two hours.

;; I initially had the canvas in the main-panel in this ns, but since I needed it
;; to load before I define canvas and ctx so I set it up temporarily
;; on index.html the bug was there before the move.
;; 

;; the more I increase the width and height of the canvas, 
;; the more zommed out I am. It is like it keeps the original 
;; 300px by 150px size contained in the defined height and width

;; if I set! the values after the fact, it seems to break

;; but I will stop now.


(def canvas (.getElementById js/document "main"))
#_(set! (.-width canvas) "2000px")
#_(set! (.-height canvas) "2000px")

(def ctx (.getContext canvas "2d"))
(.beginPath ctx)

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
  (.fillRect ctx 60 30 61 61)
  (.fill ctx)
  (.reset ctx)
  )

(defn define-color [fill] (set! (.-fillStyle ctx) fill))

(comment 
  
  (define-color "red")
  
  (.beginPath ctx)
  
  (.rect ctx 20 30 104 61)
  )

(defn draw-element [{:keys [shape-type x y width height radius fill]}]
  (js/console.log (str {:shape shape-type :x x :y y :widht width :h height :r radius :fill fill}))
  (define-color fill) 
  (if
    (= shape-type :rect)
    (.rect ctx x y width height)
    
    (.arc ctx x y radius 0 pi))
   (.fill ctx)
  )
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
      (.fillRect 7 6 104 61))
  )

(defn main-panel []
[:div ])
