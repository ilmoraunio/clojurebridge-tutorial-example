(ns global-growth.db.users
  (:require [yesql.core :refer [defquery defqueries]]
            [global-growth.core :as core]))

(defqueries "sql/users.sql" {:connection core/db-spec})