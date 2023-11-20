package no.kristiania.dao;


import no.kristiania.survey.UserAnswer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserAnswerDao extends PublicDao<UserAnswer>{
    public UserAnswerDao(DataSource dataSource) {
        super(dataSource);
    }

    public void save(UserAnswer userAnswer) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO user_answers (answer_id, question_id, user_survey_id, value) VALUES (?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, userAnswer.getAnswerId());
                statement.setLong(2, userAnswer.getQuestionId());
                statement.setLong(3, userAnswer.getUserSurveyId());
                statement.setString(4, userAnswer.getValue());
                statement.executeUpdate();

                try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    userAnswer.setId(generatedKeys.getLong("id"));

                }
            }
        }
    }

    @Override
    public List<UserAnswer> listAll() throws SQLException {
        return super.listAll("SELECT * FROM user_answers");
    }

    @Override
    protected UserAnswer storeResultSetData(ResultSet rs) throws SQLException {
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setId(rs.getLong("id"));
        userAnswer.setAnswerId(rs.getLong("answer_id"));
        userAnswer.setQuestionId(rs.getLong("question_id"));
        userAnswer.setUserSurveyId(rs.getLong("user_survey_id"));
        userAnswer.setValue(rs.getString("value"));
        return userAnswer;
    }

    public List<UserAnswer> retrieveAllByQuestionIdAndUserSurveyId(long questionId, long userSurveyId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from user_answers where question_id = ? and user_survey_id = ?"
            )) {
                statement.setLong(1, questionId);
                statement.setLong(2, userSurveyId);
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<UserAnswer> userAnswers = new ArrayList<>();

                    while (rs.next()) {
                        userAnswers.add(storeResultSetData(rs));
                    }
                    return userAnswers;
                }
            }
        }
    }
}
