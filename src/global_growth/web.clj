(ns global-growth.web
  (:require [global-growth.layout :as layout] 
            [ring.adapter.jetty :as jetty] 
            [ring.middleware.defaults :refer :all]
            [ring.util.response :as response]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.webjars :refer [wrap-webjars]]
            [global-growth.core :as api]
            [compojure.core :refer [defroutes GET]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [hiccup.core :as hiccup]
            [hiccup.page :as page]
            [hiccup.form :as form]
            [clj-http.client :as client]
            [cheshire.core :as json]))

;; WEB APP

(defn layout
  [title & content]
  (page/html5
    [:head
     [:title title]
     (page/include-css "//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css")
     (page/include-css "//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap-theme.min.css")
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
    [:body
     [:nav.navbar.navbar-default {:role "navigation"}
      [:div.container-fluid
       [:div.navbar-header
        [:a.navbar-brand {:href "/"} "World Bank Indicators"]]]]
     [:div.container
      content]]))

(defn ordered-list
  [coll]
  [:ol
   (for [list-item coll]
     [:li list-item])])

(defn format-indicator-value
  [value]
  (if (number? value)
    (format "%,.2f" (float value))
    (str value)))

(defn indicator-list
  [indicators]
  (ordered-list
    (for [country-pair indicators]
      (let [country (first country-pair)
            value (second country-pair)]
        (str country " (" (format-indicator-value value) ")")))))

(defn view-indicators
  [indicator1 indicator2 year]
  (let [inds1 (api/take-top-values
                (api/get-indicator-values indicator1 year) 10)
        inds2 (api/take-top-values
                (api/get-indicator-values indicator2 year) 10)
        indicator-map (into {}
                            (map (fn [indicator]
                                   [(second indicator) (first indicator)])
                                 (api/get-indicators)))]
    (layout "Sorted Indicators"
            [:h1 "Sorted Indicators"]
            [:div.row
             [:div.form-group.col-md-6
              (form/label indicator1 (get indicator-map indicator1))
              (if (empty? inds1)
                [:p "No indicator values for this year."]
                (indicator-list inds1))
              ]
             [:div.form-group.col-md-6
              (form/label indicator2 (get indicator-map indicator2))
              (if (empty? inds2)
                [:p "No indicator values for this year."]
                (indicator-list inds2))]])))

(defn hiccup-page [title]
  (page/html5 [:head
               [:title "foobar-page"]
               [:h1 title]
               (page/include-js "jquery.min.js")
               (page/include-js "app.js")
               (page/include-css "/webjars/bootstrap/css/bootstrap.min.css")]
              
              [:body
               [:div {:id "container"}
                [:p "Hello world!"]]]))

(defn selmer-page [context-map]
  (layout/render "selmer-page.html" context-map))

(defn main-page []
  (layout "World Bank Indicators"
          [:h1 "World Bank Indicators"]
          [:p "Choose one of these world development indicators."]
          (form/form-to {:role "form"} [:get "/indicators"]
                        [:div.row
                         [:div.form-group.col-md-5
                          (form/label "indicator1" "Indicator 1:  ")
                          (form/drop-down {:class "form-control"}
                                          "indicator1"
                                          (api/get-indicators))]
                         [:div.form-group.col-md-5
                          (form/label "indicator2" "Indicator 2:  ")
                          (form/drop-down {:class "form-control"}
                                          "indicator2"
                                          (api/get-indicators))]
                         [:div.form-group.col-md-2
                          (form/label "year" "Year: ")
                          (form/drop-down {:class "form-control"}
                                          "year"
                                          (reverse (range 1960 2013))
                                          2010)]]
                        (form/submit-button "Submit"))))

(defn json-content [data]
  (-> data response/response (response/content-type "application/json")))

(defroutes main-routes
  (GET "/" [] (main-page))
  (GET "/indicators" [indicator1 indicator2 year]
       (view-indicators indicator1 indicator2 year))
  (GET "/foo" [] (json-content (json/generate-string {:foo (:body (client/get "http://api.worldbank.org/countries?format=json&per_page=10" {:as :json}))})))
  (GET "/hiccup" [] (hiccup-page "Hik!"))
  (GET "/selmer" [] (selmer-page {:hello-world "Hello world!"})))

;(def handler (site main-routes))

(def app (-> main-routes
           (wrap-webjars "/webjars")
           (wrap-resource "")
           (wrap-defaults site-defaults)
           (wrap-content-type)
           (wrap-not-modified)
           wrap-json-response))

(defonce server (atom nil))

(defn stop! []
  (swap! server #(.stop %)))

(defn -main [& args]
  (if-not @server
    (reset! server (jetty/run-jetty #'app {:port 3000 :join? false}))
    (println "Already started")))
