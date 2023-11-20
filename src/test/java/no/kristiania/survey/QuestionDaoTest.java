package no.kristiania.survey;


import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.SurveyDao;
import no.kristiania.dao.UserDao;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionDaoTest {

    private final QuestionDao dao = new QuestionDao(TestDatabase.testDataSource());
    private SurveyDao surveyDao = new SurveyDao(TestDatabase.testDataSource());
    private UserDao userDao = new UserDao(TestDatabase.testDataSource());


    @Test
    void shouldRetrieveSavedQuestion() throws SQLException {
        User user = new User();
        user.setEmail(UUID.randomUUID().toString());
        user.setFirstName("test");
        user.setLastName("test");
        userDao.save(user);

        Survey survey = new Survey();
        survey.setName("Survey");
        survey.setDescription("Survey description");
        survey.setUserEmail(user.getEmail());
        surveyDao.save(survey);

        Question question = exampleQuestion(survey.getId());
        dao.save(question);
        assertThat(dao.retrieve(question.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(question);
    }

    @Test
    void shouldListAllQuestions() throws SQLException {
        User user = new User();
        user.setEmail(UUID.randomUUID().toString());
        user.setFirstName("test");
        user.setLastName("test");
        userDao.save(user);

        Survey survey = new Survey();
        survey.setName("Survey");
        survey.setDescription("Survey description");
        survey.setUserEmail(user.getEmail());
        surveyDao.save(survey);

        Question question = exampleQuestion(survey.getId());
        dao.save(question);
        Question anotherQuestion = exampleQuestion(survey.getId());
        dao.save(anotherQuestion);

        assertThat(dao.listAll())
                .extracting(Question::getId)
                .contains(question.getId(), anotherQuestion.getId());
    }

    @Test
    void shouldUpdateQuestion() throws SQLException {

        User user = new User();
        user.setEmail(UUID.randomUUID().toString());
        user.setFirstName("test");
        user.setLastName("test");
        userDao.save(user);

        Survey survey = new Survey();
        survey.setName("Survey");
        survey.setDescription("Survey description");
        survey.setUserEmail(user.getEmail());
        surveyDao.save(survey);

        Question question = exampleQuestion(survey.getId());
        dao.save(question);
        dao.update(question.getId(),"What even is this question");

        assertThat(dao.retrieve(question.getId()).getTitle().equals("What even is this question"));
    }




    public static Question exampleQuestion(long surveyId) {
        Question question = new Question();
        question.setTitle(TestDatabase.pickOne("What do you like to do?", "What is the name of your first crush?", "How mutch money do you have?", "What is your favorite color?"));
        question.setType(TestDatabase.pickOne("text", "single", "multi", "change"));
        question.setSurveyId(surveyId);
        question.setId(TestDatabase.pickOneLong(1, 2, 3, 4));
        return question;
    }
}

