package no.kristiania.dao;


import no.kristiania.survey.User;

import javax.sql.DataSource;
import java.sql.*;

import java.util.List;

public class UserDao extends PublicDao<User> {
    public UserDao(DataSource dataSource) {
        super(dataSource);
    }

    public void save(User user) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into users(first_name,last_name,email) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, user.getFirstName());
                statement.setString(2, user.getLastName());
                statement.setString(3, user.getEmail());
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    user.setId(rs.getLong("user_id"));
                }
            }
        }
    }

    public User retrieve(long id) throws SQLException {
        return retrieveById(id, "SELECT * FROM users WHERE user_id=?");
    }

    @Override
    public List<User> listAll() throws SQLException {
        return super.listAll("SELECT * FROM users");
    }


    @Override
    protected User storeResultSetData(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email")
        );

        user.setId(rs.getLong("user_id"));
        return user;
    }

    public User retrieveByEmail(String email) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from users where email = ?"
            )) {
                statement.setString(1, email);
                try (ResultSet rs = statement.executeQuery()) {

                if(rs.next()){
                    return storeResultSetData(rs);
                }else{
                    throw new SQLException("No object retrieved");
                }
                }
            }
        }
    }
}
