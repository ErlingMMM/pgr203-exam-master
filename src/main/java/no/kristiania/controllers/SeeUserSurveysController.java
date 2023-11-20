package no.kristiania.controllers;

import no.kristiania.dao.*;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.Question;
import no.kristiania.survey.Survey;
import no.kristiania.survey.UserAnswer;
import no.kristiania.survey.UserSurvey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SeeUserSurveysController implements HttpController {

    private final SurveyDao surveyDao;
    private final QuestionDao questionDao;
    private final AnswerDao answerDao;
    private final UserSurveyDao userSurveyDao;
    private final UserAnswerDao userAnswerDao;

    public SeeUserSurveysController(SurveyDao surveyDao, QuestionDao questionDao, AnswerDao answerDao, UserSurveyDao userSurveyDao, UserAnswerDao userAnswerDao) {
        this.surveyDao = surveyDao;
        this.questionDao = questionDao;
        this.answerDao = answerDao;
        this.userSurveyDao = userSurveyDao;
        this.userAnswerDao = userAnswerDao;
    }


    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {

        JSONObject json = new JSONObject();
        Map<String, String> queryMap = HttpServer.divideRequestMap(request.getMessageBody());
        long UserSurveyId = Long.parseLong(queryMap.get("id"));

        UserSurvey userSurvey = userSurveyDao.retrieve(UserSurveyId);
        Survey survey = surveyDao.retrieve(userSurvey.getSurvey_id());
        json.put("UserSurveyId", userSurvey.getId());
        json.put("SurveyId", survey.getId());
        json.put("surveyName", survey.getName());
        json.put("surveyDescription", survey.getDescription());

        JSONArray questionsJson = new JSONArray();
        List<Question> questionList = questionDao.listAllBySurveyId(survey.getId());
        for (Question question : questionList) {
            JSONObject questionWithAnswer = new JSONObject();
            questionWithAnswer.put("questionTitle",question.getTitle());
            List <UserAnswer> userAnswerList = userAnswerDao.retrieveAllByQuestionIdAndUserSurveyId(question.getId(), userSurvey.getId());
            JSONArray answersJson = new JSONArray();
            for (UserAnswer userAnswer : userAnswerList) {
                if (userAnswer.getValue().equals("on")){
                    answersJson.add(answerDao.retrieve(userAnswer.getAnswerId()).getText());
                } else {
                    answersJson.add(userAnswer.getValue());
                }
            }
            questionWithAnswer.put("UserAnswer",answersJson);
            questionsJson.add(questionWithAnswer);
        }
        json.put("questions", questionsJson);

        return new HttpMessage("HTTP/1.1 200 OK", json.toJSONString());
    }
}
