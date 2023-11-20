package no.kristiania.survey;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.util.Random;

public class TestDatabase {
    public static DataSource testDataSource() {
        JdbcDataSource database = new JdbcDataSource();
        database.setUrl("jdbc:h2:mem:surveydb;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(database).load().migrate();
        return database;
    }

    private static final Random random = new Random();

    public static String pickOne(String... alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }

    public static long pickOneLong(long... alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }

}
