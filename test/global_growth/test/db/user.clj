(ns global-growth.test.db.user
  (:require [clojure.test :refer :all] 
            [global-growth.db.user :as user]
            [global-growth.test.util :refer [db-spec create-db-tables drop-db-tables]]
            [buddy.hashers :as hashers]
            [clojure.java.jdbc :as sql]
            [yesql.core :refer [defquery defqueries]]))

(defn before [f]
  (try
    (drop-db-tables)
    (catch Exception e (println "Could not remove db tables: "  (.getMessage e)))
    (finally (println "Continue with tests")))
  (f))

(defn setup [f]
  (create-db-tables)
  (f)
  (drop-db-tables))

(use-fixtures :once before)
(use-fixtures :each setup)

(defqueries "sql/users.sql" {:connection db-spec})

(deftest smoke-test-user-dao
  (is (= 1 (create-user! {:full_name "Test user" 
                          :email "foo.bar@invalid.invalid" 
                          :password (hashers/encrypt "hunter2")})))
  (let [_ (is (= 1 (update-user! 
                     {:id 1 
                      :full_name "Test user edited" 
                      :password "foobar" 
                      :email "test.user@invalid.invalid"})))
        model (first (get-user {:id 1}))
        stripped-model (dissoc model :created)]
   
    (is 
      (instance? java.util.Date (:created model)))
   
   (is (= {:id 1,
           :full_name "Test user edited",
           :email "test.user@invalid.invalid",
           :password "foobar",
           :active true} stripped-model))))

(run-tests)