DROP TABLE IF EXISTS token CASCADE;
DROP SEQUENCE IF EXISTS token_seq CASCADE;
DROP TABLE IF EXISTS user_followers CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP SEQUENCE IF EXISTS users_seq CASCADE;
DROP TABLE IF EXISTS user_likes CASCADE;

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

CREATE TABLE user_likes (
    liker_id BIGINT,
    liked_user_id BIGINT,
    PRIMARY KEY (liker_id, liked_user_id)
);

ALTER TABLE user_followers ADD CONSTRAINT fk_user_followers_users FOREIGN KEY (users_id) REFERENCES users (id);
ALTER TABLE user_followers ADD CONSTRAINT fk_user_followers_following FOREIGN KEY (follower_id) REFERENCES users (id);
ALTER TABLE user_likes ADD CONSTRAINT fk_user_likes_users FOREIGN KEY (liker_id) REFERENCES users (id);
ALTER TABLE user_likes ADD CONSTRAINT fk_user_likes_liked_users FOREIGN KEY (liked_user_id) REFERENCES users (id);