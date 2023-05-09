(ns canvas.db
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 :db/initialize-db
 (fn [_ _]
   db/default-db))

(def default-db
  {:elements []})
