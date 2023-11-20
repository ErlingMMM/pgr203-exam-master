CREATE TABLE IF NOT EXISTS surveys
(
    survey_id serial primary key,
    name varchar(255) not null,
    description varchar(255) not null,
    user_email varchar (255) not null,
    CONSTRAINT fk_users
    FOREIGN KEY(user_email)
    REFERENCES users(email)
);