DROP TABLE IF EXISTS question;
DROP SEQUENCE IF EXISTS question_seq;
DROP TABLE IF EXISTS answer;
DROP SEQUENCE IF EXISTS answer_seq;

CREATE SEQUENCE question_seq;

CREATE TABLE question (
    id BIGINT NOT NULL,
    title VARCHAR(255),
    question_description VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE SEQUENCE answer_seq;

CREATE TABLE answer (
    id BIGINT NOT NULL,
    answer_description VARCHAR(255),
    question_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_question_id FOREIGN KEY (question_id) REFERENCES question(id)
);
