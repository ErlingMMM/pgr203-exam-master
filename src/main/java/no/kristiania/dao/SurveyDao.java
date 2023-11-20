package no.kristiania.dao;

import no.kristiania.survey.Survey;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SurveyDao extends PublicDao<Survey> {
    public SurveyDao(DataSource dataSource) {
        super(dataSource);
    }


    public void save(Survey survey) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO surveys(name,description, user_email) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)){
                statement.setString(1,survey.getName());
                statement.setString(2,survey.getDescription());
                statement.setString(3,survey.getUserEmail());
                statement.executeUpdate();

                try(ResultSet rs = statement.getGeneratedKeys()){
                    rs.next();
                    survey.setId(rs.getLong("survey_id"));
                }
            }
        }
    }

    public Survey retrieve(long id) throws SQLException {
        return retrieveById(id,"SELECT * FROM surveys WHERE survey_id = ?");
    }

    @Override
    public List<Survey> listAll() throws SQLException {
        return super.listAll("SELECT * FROM surveys");
    }





    public List<Survey> listAllWhereInputLike(String input) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM surveys WHERE description like CONCAT('%',?,'%') or name like CONCAT('%',?,'%')"
            )) {
                statement.setString(1, input);
                statement.setString(2, input);
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Survey> survey = new ArrayList<>();

                    while (rs.next()) {
                        survey.add(storeResultSetData(rs));
                    }
                    return survey;
                }
            }
        }
    }


    public List<Survey> listAllAscending() throws SQLException {
        return super.listAll("SELECT * FROM surveys ORDER BY name ASC");
    }


    public List<Survey> listAllDescending() throws SQLException {
        return super.listAll("SELECT * FROM surveys ORDER BY name DESC");
    }


    @Override
    protected Survey storeResultSetData(ResultSet rs) throws SQLException {
        Survey survey = new Survey();
        survey.setName(rs.getString("name"));
        survey.setDescription(rs.getString("description"));
        survey.setId(rs.getLong("survey_id"));
        survey.setUserEmail(rs.getString("user_email"));
        return survey;
    }

    public void update(long surveyId, String value, String field) {
        try (Connection connection = dataSource.getConnection()) {
            if (field.equals("name")) {
                try (PreparedStatement statement = connection.prepareStatement("UPDATE surveys SET name = ? WHERE survey_id = ?")) {
                    statement.setString(1, value);
                    statement.setLong(2, surveyId);
                    statement.executeUpdate();
                }
            } else if (field.equals("description")) {
                try (PreparedStatement statement = connection.prepareStatement("UPDATE surveys SET description = ? WHERE survey_id = ?")) {
                    statement.setString(1, value);
                    statement.setLong(2, surveyId);
                    statement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
