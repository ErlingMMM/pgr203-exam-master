package no.kristiania.dao;

import no.kristiania.survey.Survey;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class PublicDao<P> {
    protected final DataSource dataSource;

    public PublicDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static DataSource createDataSource() throws IOException {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("pgr203.properties")) {
            properties.load(reader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url",
                "jdbc:postgresql://localhost:5432/survey_db"
        ));
        dataSource.setUser(properties.getProperty("dataSource.user","survey_dbuser"));

        dataSource.setPassword(properties.getProperty("dataSource.password"));
        Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();

        return dataSource;
    }


    protected P retrieveById(long id, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return storeResultSetData(rs);
                    } else {
                        throw new SQLException("No object retrieved");
                    }
                }
            }
        }
    }


    public abstract List<P> listAll() throws SQLException;

    abstract protected P storeResultSetData(ResultSet rs) throws SQLException;

    protected List<P> listAll(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<P> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(storeResultSetData(rs));
                    }
                    return result;
                }
            }
        }
    }
}

