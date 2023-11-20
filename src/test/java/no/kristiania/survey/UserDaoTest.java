package no.kristiania.survey;

import no.kristiania.dao.UserDao;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {

    private final UserDao dao = new UserDao(TestDatabase.testDataSource());

    @Test
    public void shouldReturnSavedUser() throws SQLException {
        User user = new User("Tom","Test","tomtest@gmail.com");
        dao.save(user);

        assertThat(dao.retrieve(user.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }
}
