package no.kristiania.dao;

import no.kristiania.survey.QuestionType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class QuestionTypeDao extends PublicDao<String>{
    public QuestionTypeDao(DataSource dataSource) {
        super(dataSource);
    }


    public void save(QuestionType type) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into question_type (type) values (?)"

            )) {
                statement.setString(1, type.getType());

                statement.executeUpdate();
            }
        }
    }


    @Override
    public List<String> listAll() throws SQLException {
        return super.listAll("SELECT * FROM question_type");
    }

    @Override
    protected String storeResultSetData(ResultSet rs) throws SQLException {
        return rs.getString("type");
    }
}
