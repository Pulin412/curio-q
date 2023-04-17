DROP TABLE IF EXISTS answer CASCADE;
DROP TABLE IF EXISTS question CASCADE;
DROP SEQUENCE IF EXISTS answer_seq CASCADE;
DROP SEQUENCE IF EXISTS question_seq CASCADE;

CREATE SEQUENCE question_seq;

CREATE TABLE question (
    id BIGINT NOT NULL,
    title VARCHAR(255),
    question_description VARCHAR(255),
    userId BIGINT NOT NULL,
    userEmail VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE SEQUENCE answer_seq;

CREATE TABLE answer (
    id BIGINT NOT NULL,
    answer_description VARCHAR(255),
    question_id BIGINT,
    userId BIGINT NOT NULL,
    userEmail VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_question_id FOREIGN KEY (question_id) REFERENCES question(id)
);
