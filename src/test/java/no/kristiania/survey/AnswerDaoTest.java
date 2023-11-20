package no.kristiania.survey;

import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.SurveyDao;
import no.kristiania.dao.UserDao;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerDaoTest {
    private final AnswerDao dao = new AnswerDao(TestDatabase.testDataSource());
    private SurveyDao surveyDao = new SurveyDao(TestDatabase.testDataSource());
    private QuestionDao questionDao = new QuestionDao(TestDatabase.testDataSource());
    private AnswerDao answerDao = new AnswerDao(TestDatabase.testDataSource());
    private UserDao userDao = new UserDao(TestDatabase.testDataSource());


    @Test
    void shouldListAllAnswers() throws SQLException {
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

        Question question = new Question();
        question.setTitle("What is your favorite color?");
        question.setType("1");
        question.setSurveyId(survey.getId());
        questionDao.save(question);

        Answer answer1 = new Answer();
        answer1.setText("Blue");
        answer1.setQuestionId(question.getId());
        answerDao.save(answer1);

        Answer answer2 = new Answer();
        answer2.setText("Red");
        answer2.setQuestionId(question.getId());
        answerDao.save(answer2);

        Answer answer3 = new Answer();
        answer3.setText("Green");
        answer3.setQuestionId(question.getId());
        answerDao.save(answer3);

        List<Answer> answerList = answerDao.listAll();

        assertThat(answerList)
                .extracting(Answer::getId)
                .contains(answer1.getId(), answer2.getId(), answer3.getId());

    }

    @Test
    void shouldRetrieveSavedAnswer() throws SQLException {
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

        Question question = new Question();
        question.setTitle("What is your favorite color?");
        question.setType("1");
        question.setSurveyId(survey.getId());
        questionDao.save(question);

        Answer answer = exampleAnswer(question.getId());
        dao.save(answer);
        assertThat(dao.retrieve(answer.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(answer);
    }

    @Test
    void shouldUpdateAnswer() throws SQLException {
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

        Question question = new Question();
        question.setTitle("What is your favorite color?");
        question.setType("1");
        question.setSurveyId(survey.getId());
        questionDao.save(question);

        Answer answer = new Answer();
        answer.setText("Blue");
        answer.setQuestionId(question.getId());
        answerDao.save(answer);

        answerDao.update(answer.getId(), "Red");

        assertThat(answerDao.retrieve(answer.getId()).getText()).isEqualTo("Red");
    }



    public static Answer exampleAnswer(long questionId) {
        Answer answer = new Answer();
        answer.setText(TestDatabase.pickOne("What do you like to do?", "What is the name of your first crush?", "How mutch money do you have?", "What is your favorite color?"));
        answer.setQuestionId(questionId);
        return answer;
    }

}
