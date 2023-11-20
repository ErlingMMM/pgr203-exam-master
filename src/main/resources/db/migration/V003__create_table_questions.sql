CREATE TABLE IF NOT EXISTS questions
(
    question_id serial primary key,
    title varchar(255) not null,
    type varchar(255) not null,
    survey_id int,
    CONSTRAINT fk_surveys
        FOREIGN KEY(survey_id)
            REFERENCES surveys(survey_id)
);