package no.kristiania.dao;


import no.kristiania.survey.UserSurvey;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSurveyDao extends PublicDao<UserSurvey> {

    public UserSurveyDao(DataSource dataSource) {
        super(dataSource);
    }

    public UserSurvey retrieve(long surveyId) throws SQLException {
        return retrieveById(surveyId, "SELECT * FROM user_surveys WHERE id = ?");
    }

    public void save(UserSurvey userSurvey) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO user_surveys (survey_id, user_email) VALUES (?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, userSurvey.getSurvey_id());
                statement.setString(2, userSurvey.getUserEmail());
                statement.executeUpdate();

                try(ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    userSurvey.setId(rs.getLong("id"));
                    userSurvey.setCreated_at(rs.getTimestamp("created_at"));
                }
            }

        }
    }

    public List<UserSurvey> listAllBySurveyId(Long surveyId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_surveys WHERE survey_id = ?")) {
                statement.setLong(1, surveyId);
                try (ResultSet rs = statement.executeQuery()) {
                    List<UserSurvey> userSurveys = new ArrayList<>();
                    while(rs.next()){
                        userSurveys.add(storeResultSetData(rs));
                    }

                    return userSurveys;
                }
            }
        }
    }

    @Override
    public List<UserSurvey> listAll() throws SQLException {
        return super.listAll("SELECT * FROM user_surveys");
    }

    @Override
    protected UserSurvey storeResultSetData(ResultSet rs) throws SQLException {
        UserSurvey userSurvey = new UserSurvey();
        userSurvey.setId(rs.getLong("id"));
        userSurvey.setSurvey_id(rs.getLong("survey_id"));
        userSurvey.setUserEmail(rs.getString("user_email"));
        userSurvey.setCreated_at(rs.getTimestamp("created_at"));
        return userSurvey;
    }
}
