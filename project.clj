(defproject global-growth "0.1.0-SNAPSHOT"
  :description "Demonstrates the use of the World Bank API"
  :url "https://github.com/clojurebridge/global-growth"
  :license {:name "Creative Commons Attribution License"
            :url "http://creativecommons.org/licenses/by/3.0/"}
  :plugins [[lein-ring "0.8.10" :exclusions [org.clojure/clojure]]]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-http "2.0.0"]
                 [cheshire "5.5.0"]
                 [ring "1.4.0"] 
                 [ring/ring-json "0.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [ring-webjars "0.1.1"]
                 [org.webjars/bootstrap "3.3.5"]
                 [selmer "0.9.3"]
                 [org.slf4j/slf4j-log4j12 "1.7.1"]
                 [com.h2database/h2 "1.4.190"]
                 [yesql "0.5.1"]
                 [prismatic/schema "1.0.3"]
                 [buddy "0.6.0"]
                 [ring/ring-anti-forgery "1.0.0"]]
  ;:jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"]
  :min-lein-version "2.0.0"
  :main ^:skip-aot global-growth.web
  :ring {:handler global-growth.web/app}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
