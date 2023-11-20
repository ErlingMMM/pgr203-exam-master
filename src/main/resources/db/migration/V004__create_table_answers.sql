CREATE TABLE IF NOT EXISTS answers
(
    answer_id serial primary key,
    text varchar(255) not null,
    question_id int not null,
    CONSTRAINT fk_questions
        FOREIGN KEY(question_id)
            REFERENCES questions(question_id)
);