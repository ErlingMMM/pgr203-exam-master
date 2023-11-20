CREATE TABLE IF NOT EXISTS user_answers
(
    id         SERIAL PRIMARY KEY,
    answer_id  int   NOT NULL,
    question_id int   NOT NULL,
    user_survey_id int NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    value      VARCHAR(255) NOT NULL,

    CONSTRAINT answer_id_fk FOREIGN KEY (answer_id) REFERENCES Answers (answer_id),
    CONSTRAINT question_id_fk FOREIGN KEY (question_id) REFERENCES Questions (question_id),
    CONSTRAINT user_survey_id_fk FOREIGN KEY (user_survey_id) REFERENCES user_surveys (id)

);