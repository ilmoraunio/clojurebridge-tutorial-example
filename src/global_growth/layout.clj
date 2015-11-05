(ns global-growth.layout
  (:require [selmer.parser :as parser]
            [ring.util.response :refer [content-type response]]))

(parser/set-resource-path!  (clojure.java.io/resource "templates"))

(defn render [template & [params]]
  (-> template
      (parser/render-file
        (assoc params
          :page template
          :csrf-token "todo"))
      response
      (content-type "text/html; charset=utf-8")))