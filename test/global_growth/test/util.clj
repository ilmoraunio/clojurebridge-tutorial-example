(ns global-growth.test.util
  (:require [clojure.java.jdbc :as sql]))

(def db-spec {:classname    "org.h2.Driver"
              :subprotocol  "h2"
              :subname      "./resources/test.db"
              :user         "sa"
              :password     "" })

(defn create-db-tables []
  (sql/db-do-commands db-spec
    (sql/create-table-ddl :users
      [:id "bigint primary key auto_increment"]
      [:full_name "varchar(2056)"]
      [:email "varchar(2056)" "NOT NULL"]
      [:password "varchar(2056)" "NOT NULL"]
      [:active "boolean" "DEFAULT 1"]
      [:created "timestamp" "DEFAULT CURRENT_TIMESTAMP()"])))

(defn drop-db-tables []
  (sql/db-do-commands db-spec (sql/drop-table-ddl :users)))