package no.kristiania.survey;

import no.kristiania.dao.SurveyDao;
import no.kristiania.dao.UserDao;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

public class SurveyDaoTest {

    private SurveyDao surveyDao = new SurveyDao(TestDatabase.testDataSource());
    private UserDao userDao = new UserDao(TestDatabase.testDataSource());

    @Test
    public void shouldListAllWhereInputLike() throws SQLException {
        User user = new User();
        user.setEmail("test");
        user.setFirstName("test");
        user.setLastName("test");
        userDao.save(user);

        Survey survey1 = new Survey();
        survey1.setName("Cats");
        survey1.setDescription("Survey all about cats");
        survey1.setUserEmail(user.getEmail());
        surveyDao.save(survey1);

        Survey survey2 = new Survey();
        survey2.setName("Dogs");
        survey2.setDescription("Survey all about dogs");
        survey2.setUserEmail(user.getEmail());
        surveyDao.save(survey2);

        assertThat(surveyDao.listAllWhereInputLike("Dogs"))
                .extracting(Survey::getName)
                .containsExactly("Dogs");

    }

    @Test
    void shouldListAllAscending() throws SQLException {

        assertThat(surveyDao.listAllAscending())
                .extracting(Survey::getName)
                .isSorted();
    }

    @Test
    void shouldListAllDescending() throws SQLException {

        assertThat(surveyDao.listAllDescending())
                .extracting(Survey::getName)
                .satisfies(names -> assertThat(names).isSortedAccordingTo(Comparator.reverseOrder()));
    }

    @Test
    void shouldUpdateSurveyName() throws SQLException {
        User user = new User();
        user.setEmail("test2");
        user.setFirstName("test");
        user.setLastName("test");
        userDao.save(user);

        Survey survey = new Survey();
        survey.setName("test3");
        survey.setDescription("test");
        survey.setUserEmail(user.getEmail());
        surveyDao.save(survey);

        surveyDao.update(survey.getId(), "Updated name", "name");

        assertThat(surveyDao.retrieve(survey.getId()))
                .extracting(Survey::getName)
                .isEqualTo("Updated name");
    }

    @Test
    void shouldUpdateSurveyDesc() throws SQLException {

        User user = new User();
        user.setEmail("test4");
        user.setFirstName("test");
        user.setLastName("test");
        userDao.save(user);

        Survey survey = new Survey();
        survey.setName("test");
        survey.setDescription("test");
        survey.setUserEmail(user.getEmail());
        surveyDao.save(survey);

        surveyDao.update(survey.getId(), "Updated description", "description");

        assertThat(surveyDao.retrieve(survey.getId()))
                .extracting(Survey::getDescription)
                .isEqualTo("Updated description");
    }

    public static Survey exampleSurvey() {
        Survey survey = new Survey();
        survey.setName(TestDatabase.pickOne("What do you like to do?", "What is the name of your first crush?", "How mutch money do you have?", "What is your favorite color?"));
        survey.setDescription(TestDatabase.pickOne("Animals", "Cats", "Politics", "Sports"));
        return survey;
    }
}
