package no.kristiania.dao;

import no.kristiania.survey.Answer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerDao extends PublicDao<Answer> {
    public AnswerDao(DataSource dataSource) {
        super(dataSource);
    }


    public void save(Answer answer) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into answers (text, question_id) values (?,?)",
                    Statement.RETURN_GENERATED_KEYS
                    )) {
                statement.setString(1, answer.getText());
                statement.setLong(2,answer.getQuestionId());
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    answer.setId(rs.getLong("answer_id"));
                }
            }
        }
    }


    public Answer retrieve(long id) throws SQLException {
        return retrieveById(id, "select * from answers where answer_id = ?");
    }

    public void update(long answerId, String newValue) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE answers SET text = ? WHERE answer_id = ?"
            )) {
                statement.setString(1, newValue);
                statement.setLong(2, answerId);
                statement.executeUpdate();
            }
        }
    }



    public List<Answer> listByQuestionId(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM answers WHERE question_id = ?"
            )) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Answer> answers = new ArrayList<>();

                    while (rs.next()) {
                        answers.add(storeResultSetData(rs));
                    }
                    return answers;
                }
            }
        }
    }


    @Override
    public List<Answer> listAll() throws SQLException {
        return super.listAll("SELECT * FROM answers");
    }

    @Override
    protected Answer storeResultSetData(ResultSet rs) throws SQLException {
        Answer answer = new Answer();
        answer.setId(rs.getLong("answer_id"));
        answer.setText(rs.getString("text"));
        answer.setQuestionId(Integer.parseInt(rs.getString("question_id")));
        return answer;
    }

}

