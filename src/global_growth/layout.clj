(ns global-growth.layout
  (:require [selmer.parser :as parser]
            [ring.util.response :refer [content-type response]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(parser/set-resource-path!  (clojure.java.io/resource "templates"))

(parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))

(defn render [template & [params]]
  (-> template
      (parser/render-file
        (assoc params
          :page template))
      response
      (content-type "text/html; charset=utf-8")))