package no.kristiania.http;

import no.kristiania.controllers.*;
import no.kristiania.dao.*;
import no.kristiania.survey.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import java.time.LocalTime;


import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {
    private final HttpServer server = new HttpServer(0);

    public HttpServerTest() throws IOException {
    }

    @Test
    public void shouldReturn404ForUnknownRequestTarget() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/non-existing");
        assertEquals(404, client.getStatusCode());
    }

    @Test
    public void shouldServeFiles() throws IOException {
        Paths.get("target/test-classes");

        String fileContent = "New file created with the timestamp: " + LocalTime.now();
        Files.write(Paths.get("target/test-classes/test-file.txt"), fileContent.getBytes(StandardCharsets.UTF_8));
        HttpClient client = new HttpClient("localhost", server.getPort(), "/test-file.txt");
        assertEquals(fileContent, client.getMessageBody());
        assertEquals("text/plain; charset=utf-8", client.getHeader("Content-Type"));
    }

    @Test
    public void shouldUseFileExtensionForHtmlFile() throws IOException {
        Paths.get("target/test-classes");

        String fileContent = "<p>Hello</p>";
        Files.write(Paths.get("target/test-classes/test-file.html"), fileContent.getBytes(StandardCharsets.UTF_8));
        HttpClient client = new HttpClient("localhost", server.getPort(), "/test-file.html");
        assertEquals("text/html; charset=utf-8", client.getHeader("Content-Type"));
    }


    @Test
    public void shouldHandleMoreThanOneRequest() throws IOException {
        assertEquals(200, new HttpClient("localhost", server.getPort(), "/index.html")
                .getStatusCode());
        assertEquals(200, new HttpClient("localhost", server.getPort(), "/index.html")
                .getStatusCode());
    }


    @Test
    void shouldCreateNewUser() throws IOException, SQLException {
        UserDao userDao = new UserDao(TestDatabase.testDataSource());
        server.addController("/api/newUser", new CreateUserController(userDao));


        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newUser",
                "lastName=Persson&firstName=Test&email=test@nomail.com"
        );
        assertEquals(200, postClient.getStatusCode());

        assertThat(userDao.listAll())
                .anySatisfy(p -> {
                    assertThat(p.getFirstName()).isEqualTo("Test");
                    assertThat(p.getLastName()).isEqualTo("Persson");
                    assertThat(p.getEmail()).isEqualTo("test@nomail.com");
                });
    }


    @Test
    void shouldCreateNewSurvey() throws IOException, SQLException {
        SurveyDao surveyDao = new SurveyDao(TestDatabase.testDataSource());
        QuestionDao questionDao = new QuestionDao(TestDatabase.testDataSource());
        AnswerDao answerDao = new AnswerDao(TestDatabase.testDataSource());
        server.addController("/api/newSurvey", new NewSurveyController(surveyDao, questionDao, answerDao));
        UserDao userDao = new UserDao(TestDatabase.testDataSource());

        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Persson");
        user.setEmail("test@test.com");
        userDao.save(user);


        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newSurvey",
                "{\"survey-title\":\"Test\",\"survey-description\":\"Survey\",\"userEmail\":\"test@test.com\"}"
        );
        assertEquals(200, postClient.getStatusCode());

        assertThat(surveyDao.listAll())
                .anySatisfy(p -> {
                    assertThat(p.getName()).isEqualTo("Test");
                    assertThat(p.getDescription()).isEqualTo("Survey");
                });
    }


    @Test
    void shouldListQuestionsFromDatabase() throws IOException, SQLException {

        QuestionDao questionDao = new QuestionDao(TestDatabase.testDataSource());
        SurveyDao surveyDao = new SurveyDao(TestDatabase.testDataSource());
        AnswerDao answerDao = new AnswerDao(TestDatabase.testDataSource());
        UserDao userDao = new UserDao(TestDatabase.testDataSource());
        server.addController("takeSurvey.html", new TakeSurveyController(questionDao, surveyDao, answerDao));

        User user = new User();
        user.setEmail(UUID.randomUUID().toString());
        user.setFirstName("test");
        user.setLastName("test");
        userDao.save(user);

        Survey survey1 = SurveyDaoTest.exampleSurvey();
        survey1.setUserEmail(user.getEmail());
        surveyDao.save(survey1);

        Question question1 = QuestionDaoTest.exampleQuestion(survey1.getId());
        Question question2 = QuestionDaoTest.exampleQuestion(survey1.getId());
        questionDao.save(question1);
        questionDao.save(question2);


        HttpClient client = new HttpClient("localhost", server.getPort(), "takeSurvey.html?id=" + survey1.getId());
        assertEquals(200, client.getStatusCode());
        assertThat(client.getMessageBody())
                .contains(question1.getTitle())
                .contains(question2.getTitle())

        ;
    }


    @Test
    void shouldListSurvey() throws SQLException, IOException {
        SurveyDao surveyDao = new SurveyDao(TestDatabase.testDataSource());
        UserDao userDao = new UserDao(TestDatabase.testDataSource());
        server.addController("/api/listSurvey", new ListSurveyController(surveyDao, userDao));


        User user = new User();
        user.setEmail(UUID.randomUUID().toString());
        user.setFirstName("test");
        user.setLastName("test");
        userDao.save(user);

        Survey survey1 = new Survey();
        survey1.setName("Test");
        survey1.setDescription("Survey");
        survey1.setUserEmail(user.getEmail());
        surveyDao.save(survey1);

        Survey survey2 = new Survey();
        survey2.setName("Test1");
        survey2.setDescription("Survey1");
        survey2.setUserEmail(user.getEmail());
        surveyDao.save(survey2);

        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/listSurvey");
        assertThat(client.getMessageBody())

                .contains("<div onClick=\"takeSurvey(" + survey1.getId() + ")\"  class=\"survey\">Name of survey: <br> " + survey1.getName() + "<br><br> the topic for this survey is: " + survey1.getDescription() + "<br><br> This survey was created by:<br>" + userDao.retrieveByEmail(survey1.getUserEmail()).getFirstName() + ", " + userDao.retrieveByEmail(survey1.getUserEmail()).getLastName() + "<br><br> Contact:<br>" + survey1.getUserEmail() + "</div>" + "<div onClick=\"editSurvey(" + survey1.getId() + ")\"class=\"survey\"><p>Edit this survey</p>" + "</div>" + "<div class=\"see-answers-btn\" onClick=\"seeSurveyAnswers(" + survey1.getId() + ")\"><p>See all answers for this survey</p></div><br>")
                .contains("<div onClick=\"takeSurvey(" + survey2.getId() + ")\"  class=\"survey\">Name of survey: <br> " + survey2.getName() + "<br><br> the topic for this survey is: " + survey2.getDescription() + "<br><br> This survey was created by:<br>" + userDao.retrieveByEmail(survey2.getUserEmail()).getFirstName() + ", " + userDao.retrieveByEmail(survey2.getUserEmail()).getLastName() + "<br><br> Contact:<br>" + survey2.getUserEmail() + "</div>" + "<div onClick=\"editSurvey(" + survey2.getId() + ")\"class=\"survey\"><p>Edit this survey</p>" + "</div>" + "<div class=\"see-answers-btn\" onClick=\"seeSurveyAnswers(" + survey2.getId() + ")\"><p>See all answers for this survey</p></div><br>");
    }


    @Test
    void shouldListSavedUserSurveys() throws SQLException, IOException {
        UserSurveyDao userSurveyDao = new UserSurveyDao(TestDatabase.testDataSource());
        SurveyDao surveyDao = new SurveyDao(TestDatabase.testDataSource());
        UserDao userDao = new UserDao(TestDatabase.testDataSource());

        User user = new User();
        user.setEmail(UUID.randomUUID().toString());
        user.setFirstName("test");
        user.setLastName("test");
        userDao.save(user);

        Survey survey = new Survey();
        survey.setName("Test");
        survey.setDescription("Survey");
        survey.setUserEmail(user.getEmail());
        surveyDao.save(survey);

        UserSurvey userSurvey = new UserSurvey();
        userSurvey.setSurvey_id(survey.getId());
        userSurvey.setUserEmail(user.getEmail());
        userSurveyDao.save(userSurvey);

        server.addController("/api/seeSurveyAnswers", new SeeSurveyEntriesController(userSurveyDao));
        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/seeSurveyAnswers?id=" + survey.getId());
        assertEquals(200, client.getStatusCode());
        assertThat(client.getMessageBody())
                .contains("{\"surveyId\":" + survey.getId() + ",\"entries\":[{\"createdAt\":\"" + userSurvey.getCreated_at() + "\",\"userEmail\":\""+ userSurvey.getUserEmail() +"\",\"id\":" + userSurvey.getId() + "}]}");

    }

    @Test
    void shouldListSavedUserAnswers() throws IOException, SQLException {
        UserAnswerDao userAnswerDao = new UserAnswerDao(TestDatabase.testDataSource());
        UserSurveyDao userSurveyDao = new UserSurveyDao(TestDatabase.testDataSource());
        QuestionDao questionDao = new QuestionDao(TestDatabase.testDataSource());
        AnswerDao answerDao = new AnswerDao(TestDatabase.testDataSource());
        SurveyDao surveyDao = new SurveyDao(TestDatabase.testDataSource());
        UserDao userDao = new UserDao(TestDatabase.testDataSource());

        User user = new User();
        user.setEmail(UUID.randomUUID().toString());
        user.setFirstName("test");
        user.setLastName("test");
        userDao.save(user);

        Survey survey = new Survey();
        survey.setName("Test");
        survey.setDescription("Survey");
        survey.setUserEmail(user.getEmail());
        surveyDao.save(survey);

        Question question = new Question();
        question.setSurveyId(survey.getId());
        question.setTitle("Test Question");
        question.setType("text");
        questionDao.save(question);

        Answer answer = new Answer();
        answer.setQuestionId(question.getId());
        answer.setText("Test Answer");
        answerDao.save(answer);

        UserSurvey userSurvey = new UserSurvey();
        userSurvey.setSurvey_id(survey.getId());
        userSurvey.setUserEmail(user.getEmail());
        userSurveyDao.save(userSurvey);

        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setUserSurveyId(userSurvey.getId());
        userAnswer.setQuestionId(question.getId());
        userAnswer.setAnswerId(answer.getId());
        userAnswer.setValue("test user answer");
        userAnswerDao.save(userAnswer);


        server.addController("/api/seeSurveyAnswers", new SeeUserSurveysController(surveyDao, questionDao, answerDao, userSurveyDao, userAnswerDao));
        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/seeSurveyAnswers?id=" + userSurvey.getId());
        assertEquals(200, client.getStatusCode());
        assertThat(client.getMessageBody())
                .contains("{\"surveyDescription\":\"" + survey.getDescription() + "\",\"SurveyId\":" + survey.getId() + ",\"surveyName\":\"" + survey.getName() + "\",\"questions\":[{\"questionTitle\":\"" + question.getTitle() + "\",\"UserAnswer\":[\"" + userAnswer.getValue() + "\"]}],\"UserSurveyId\":" + userSurvey.getId() + "}");
    }
}


