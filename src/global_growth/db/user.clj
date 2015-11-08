(ns global-growth.db.user
  (:require [yesql.core :refer [defquery defqueries]]
            [global-growth.core :as core]))

(defqueries "sql/users.sql" {:connection core/db-spec})