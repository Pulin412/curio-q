DROP TABLE IF EXISTS token;
DROP SEQUENCE IF EXISTS token_seq;
DROP TABLE IF EXISTS user_followers;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS users_seq;

CREATE SEQUENCE token_seq;

CREATE TABLE token (
    id BIGINT PRIMARY KEY DEFAULT nextval('token_seq'),
    token VARCHAR(255) UNIQUE,
    token_type VARCHAR(255),
    revoked BOOLEAN,
    expired BOOLEAN,
    user_id VARCHAR(255)
);

CREATE SEQUENCE users_seq;

CREATE TABLE users (
    id BIGINT PRIMARY KEY DEFAULT nextval('users_seq'),
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255),
    token VARCHAR(255)
);

CREATE TABLE user_followers (
    users_id BIGINT,
    follower_id BIGINT
);

ALTER TABLE user_followers ADD CONSTRAINT fk_user_followers_users FOREIGN KEY (users_id) REFERENCES users (id);
ALTER TABLE user_followers ADD CONSTRAINT fk_user_followers_following FOREIGN KEY (follower_id) REFERENCES users (id);

-- Set the baseline version
--INSERT INTO schema_version (version, description, type, script, checksum, installed_by, installed_on, execution_time, success)
--VALUES ('1', 'Initial schema', 'SQL', 'V1__initial_schema.sql', NULL, 'Flyway', NOW(), 0, TRUE);