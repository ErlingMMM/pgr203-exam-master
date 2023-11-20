package no.kristiania.http;


import no.kristiania.controllers.*;
import no.kristiania.dao.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;


public class Main {


    public static void main(String[] args) throws IOException, SQLException {
        HttpServer httpServer = new HttpServer(1967);
        handleController(httpServer);
        HttpServer.logger.info("Started http://localhost:{}/index.html", httpServer.getPort());
    }

    private static void handleController(HttpServer httpServer) throws IOException, SQLException {
        DataSource dataSource = PublicDao.createDataSource();
        SurveyDao surveyDao = new SurveyDao(dataSource);
        QuestionDao questionDao = new QuestionDao(dataSource);
        AnswerDao answerDao = new AnswerDao(dataSource);
        QuestionTypeDao questionTypeDao = new QuestionTypeDao(dataSource);
        UserDao userDao = new UserDao(dataSource);
        UserAnswerDao userAnswerDao = new UserAnswerDao(dataSource);
        UserSurveyDao userSurveyDao = new UserSurveyDao(dataSource);
        httpServer.addController("/api/newSurvey", new NewSurveyController(surveyDao, questionDao, answerDao));
        httpServer.addController("/api/listSurvey", new ListSurveyController(surveyDao, userDao));
        httpServer.addController("/api/questionTypes", new QuestionTypeController(questionTypeDao));
        httpServer.addController("/api/takeSurvey", new TakeSurveyController(questionDao,surveyDao,answerDao));
        httpServer.addController("/api/filterSurvey", new FilterSurveyController(surveyDao));
        httpServer.addController("/api/newUser", new CreateUserController(userDao));
        httpServer.addController("/api/printUserName", new LoginController(userDao));
        httpServer.addController("/api/userSurvey", new UserSurveyController(userSurveyDao, userAnswerDao));
        httpServer.addController("/api/seeSurveyAnswers", new SeeSurveyEntriesController(userSurveyDao));
        httpServer.addController("/api/seeUserEntry", new SeeUserSurveysController(surveyDao, questionDao, answerDao, userSurveyDao, userAnswerDao));
        httpServer.addController("/api/editSurvey", new EditSurveyController(surveyDao, questionDao, answerDao));
        httpServer.addController("/api/updateSurveyValues", new UpdateSurveyController(answerDao, questionDao, surveyDao));
        SaveQuestionType saveQuestionType = new SaveQuestionType(questionTypeDao);
        saveQuestionType.saveQuestionsTypes();
    }
}
