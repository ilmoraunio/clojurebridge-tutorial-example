-- name: create-user!
-- Create active user.
INSERT INTO users
(id, full_name, email, password, active, created)
VALUES (:id, :full_name, :email, :password, 1, CURRENT_TIMESTAMP())

-- name: update-user!
-- Update all columns with given id.
UPDATE users
SET full_name = :full_name, email = :email, password = :password, active = :active
WHERE id = :id

-- name: get-users
SELECT *
FROM users

-- name: get-user
SELECT *
FROM users
WHERE id = :id
