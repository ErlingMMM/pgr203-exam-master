package no.kristiania.dao;

import no.kristiania.survey.Question;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao extends PublicDao<Question>{
    public QuestionDao(DataSource dataSource) {
        super(dataSource);
    }



    public void save(Question question) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement
                    ("insert into questions (title, type, survey_id) values (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, question.getTitle());
                statement.setString(2, question.getType());
                statement.setLong(3, question.getSurveyId());



                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    question.setId(rs.getLong("question_id"));
                }

            }
        }
    }


    public Question retrieve(long id) throws SQLException {
        return retrieveById(id, "select * from questions where question_id = ?");
    }


    public List<Question> listAllBySurveyId(long id ) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questions where survey_id = ?"
            )) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Question> questions = new ArrayList<>();

                    while (rs.next()) {
                        questions.add(storeResultSetData(rs));
                    }
                    return questions;
                }
            }
        }
    }





    @Override
    public List<Question> listAll() throws SQLException {
        return super.listAll("SELECT * FROM questions");
    }




    @Override
    protected Question storeResultSetData(ResultSet rs) throws SQLException {
        Question questions = new Question();
        questions.setId(rs.getLong("question_id"));
        questions.setTitle(rs.getString("title"));
        questions.setType(rs.getString("type"));
        questions.setSurveyId(rs.getLong("survey_id"));
        return questions;
    }


    public void update(long questionsId, String value) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("update questions set title = ? where question_id = ?")) {
                statement.setString(1, value);
                statement.setLong(2, questionsId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
