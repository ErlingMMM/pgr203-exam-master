Create TABLE IF NOT EXISTS user_surveys
(
    id         SERIAL PRIMARY KEY,
    survey_id  int   NOT NULL,
    user_email varchar(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    constraint user_email_fk FOREIGN KEY (user_email) references users (email),
    CONSTRAINT survey_id_fk FOREIGN KEY (survey_id) REFERENCES Surveys (survey_id)
);