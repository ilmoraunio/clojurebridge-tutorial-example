(ns global-growth.test.service.user
  (:require [clojure.test :refer :all]
            [global-growth.service.user :as service]
            [global-growth.db.user :refer [get-user]]
            [schema.core :as s]
            [schema.experimental.complete :as c]
            [schema.experimental.generators :as g]
            [global-growth.db.user :refer [get-user]]))

(deftest test-get-user
  (testing "Get single user, mock persistence"
           (with-redefs [get-user (fn [_] (-> (g/generate service/UserModel)
                                              first ; always pick just one
                                              vector))] ; not concurrency friendly!
             (let [model (service/get-user 1)]
               (s/validate service/User-out model)))))
  
(run-tests)