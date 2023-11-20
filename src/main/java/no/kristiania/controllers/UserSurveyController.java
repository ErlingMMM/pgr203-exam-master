package no.kristiania.controllers;

import no.kristiania.dao.UserAnswerDao;
import no.kristiania.dao.UserSurveyDao;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.UserAnswer;
import no.kristiania.survey.UserSurvey;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.sql.SQLException;
import java.util.Map;

public class UserSurveyController implements HttpController {
    private final UserSurveyDao userSurveyDao;
    private final UserAnswerDao userAnswerDao;

    public UserSurveyController(UserSurveyDao userSurveyDao, UserAnswerDao userAnswerDao) {
        this.userSurveyDao = userSurveyDao;
        this.userAnswerDao = userAnswerDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(request.messageBody);
            String userEmail = jsonObject.get("userEmail").toString();
            long surveyId = Long.parseLong(jsonObject.get("surveyId").toString());

            JSONObject userAnswers = (JSONObject) jsonObject.get("data");

            UserSurvey userSurvey = new UserSurvey();
            userSurvey.setSurvey_id(surveyId);
            userSurvey.setUserEmail(userEmail);
            userSurveyDao.save(userSurvey);

            Map<String, String> userAnswersMap = HttpServer.divideRequestMap(userAnswers.toJSONString());
            for (int i=0; i<userAnswersMap.size(); i++) {
                String key = userAnswersMap.keySet().toArray()[i].toString();
                String value = userAnswersMap.values().toArray()[i].toString();

                long questionId = Long.parseLong(key.substring(key.indexOf("q")+1,key.indexOf("a")-1));
                long answerId = Long.parseLong(key.substring(key.indexOf("a")+1));

                UserAnswer userAnswer = new UserAnswer();
                userAnswer.setValue(value);
                userAnswer.setAnswerId(answerId);
                userAnswer.setQuestionId(questionId);
                userAnswer.setUserSurveyId(userSurvey.getId());
                try {
                    userAnswerDao.save(userAnswer);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            };



        } catch (Exception e) {
            return new HttpMessage("400 bad request", "Invalid JSON");
        }
        return new HttpMessage("HTTP/1.1 200 OK", "success");
    }
}
