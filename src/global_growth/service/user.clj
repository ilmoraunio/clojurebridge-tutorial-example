(ns global-growth.service.user
    (:require [global-growth.core :as core]
              [global-growth.db.user :as user]
              [schema.core :as s]
              [schema.coerce :as coerce]
              [buddy.hashers :as hashers]))

(def UserModel
  "A schema for User model."
  [{:id s/Int, :full_name s/Str,
   :email s/Str, :password s/Str,
   :active s/Bool, :created s/Inst}])

(def User-in
  {:full_name s/Str, :email s/Str :password s/Str})

(def ExistingUser
  (assoc User-in :id s/Int))

(def User-out
  [{:id s/Int, :full_name s/Str, :email s/Str}])

(defn UserModel->User-out [user-model]
  (map #(select-keys % [:id :full_name :email]) user-model))

(def UserModel->User-out-coercer
  (coerce/coercer User-out {User-out UserModel->User-out}))

(defn hash-password [user]
  (assoc user
         :password (hashers/encrypt (:password user))))

(defn get-user 
  ([] 
    (let [user (user/get-users)]
      (UserModel->User-out-coercer user)))
  ([id] 
    (let [user (user/get-user {:id id})]
      (UserModel->User-out-coercer user))))

(defn update-user! [user]
  (s/validate ExistingUser user)
  (let [hashed-user (hash-password user)]
    (user/update-user! hashed-user)))

(defn create-user! [user]
  (s/validate User-in user)
  (let [hashed-user (hash-password user)]
    (user/create-user! user)))