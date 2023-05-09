(ns canvas.views
  (:require
   [re-frame.core :as rf]))



(defn main-panel []
  (let [name "names"]
    [:div
     [:h1
      "Hello from " name]
     ]))
