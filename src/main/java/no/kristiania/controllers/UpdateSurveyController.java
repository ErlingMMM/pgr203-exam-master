package no.kristiania.controllers;

import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.SurveyDao;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.Answer;
import no.kristiania.survey.Question;
import no.kristiania.survey.Survey;


import java.sql.SQLException;
import java.util.Map;

public class UpdateSurveyController implements HttpController{
    private AnswerDao answerDao;
    private QuestionDao questionDao;
    private SurveyDao surveyDao;

    public UpdateSurveyController(AnswerDao answerDao, QuestionDao questionDao, SurveyDao surveyDao) {
        this.answerDao = answerDao;
        this.questionDao = questionDao;
        this.surveyDao = surveyDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpServer.divideRequestMap(request.getMessageBody());

        queryMap.forEach((key, value) -> {
            if (key.contains("q") && key.contains("a")) {
                long answerId = Long.parseLong(key.substring(key.indexOf("a")+1));

                try {
                    Answer currentAnswer = answerDao.retrieve(answerId);

                    if (currentAnswer.getText().equals(value)) {
                        return;
                    } else {
                        answerDao.update(answerId, value);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (key.contains("q")) {
                long questionsId = Long.parseLong(key.substring(key.indexOf("q")+2));

                try {
                    Question currentQuestion = questionDao.retrieve(questionsId);

                    if (currentQuestion.getTitle().equals(value)) {
                        return;
                    } else {
                        questionDao.update(questionsId, value);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (key.contains("surveyName")) {
                try {
                    Survey CurrentSurvey = surveyDao.retrieve(Long.parseLong(queryMap.get("surveyId")));
                    if (CurrentSurvey.getName().equals(value)) {
                        return;
                    } else {
                        surveyDao.update(Long.parseLong(queryMap.get("surveyId")), value, "name");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (key.contains("surveyDescription")) {
                try {
                    Survey CurrentSurvey = surveyDao.retrieve(Long.parseLong(queryMap.get("surveyId")));
                    if (CurrentSurvey.getDescription().equals(value)) {
                        return;
                    } else {
                        surveyDao.update(Long.parseLong(queryMap.get("surveyId")), value, "description");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Something went wrong");
            }
        });

        return new HttpMessage("HTTP/1.1 200 OK","Success");
    }
}
