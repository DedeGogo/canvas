(ns canvas.db
  (:require [re-frame.core :as rf]
            [canvas.data :refer [data]]))

(rf/reg-event-db
 :db/initialize-db
 (fn [_ _]
   db/default-db))

(def default-db
  {:elements data})
