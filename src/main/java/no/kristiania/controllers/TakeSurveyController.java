package no.kristiania.controllers;



import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.SurveyDao;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.Answer;
import no.kristiania.survey.Question;
import no.kristiania.survey.Survey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class TakeSurveyController implements HttpController {

    private final QuestionDao questionDao;
    private final AnswerDao answerDao;
    private final SurveyDao surveyDao;


    public TakeSurveyController(QuestionDao questionDao, SurveyDao surveyDao, AnswerDao answerDao) {
        this.answerDao = answerDao;
        this.surveyDao = surveyDao;
        this.questionDao = questionDao;
    }



    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
            Map<String, String> queryMap = HttpServer.divideRequestMap(request.getMessageBody());
            long surveyId = Long.parseLong(queryMap.get("id"));

            Survey survey = surveyDao.retrieve(surveyId);
            List<Question> questions = questionDao.listAllBySurveyId(surveyId);

            JSONObject surveyJson = new JSONObject();
            surveyJson.put("surveyId", survey.getId());
            surveyJson.put("surveyName", survey.getName());
            surveyJson.put("surveyDescription", survey.getDescription());

            JSONArray questionsArray = new JSONArray();
            for (Question question : questions) {
                JSONObject questionObject = new JSONObject();
                questionObject.put("questionId", question.getId());
                questionObject.put("surveyId", question.getSurveyId());
                questionObject.put("questionText", question.getTitle());
                questionObject.put("questionType", question.getType());

                List<Answer> answers = answerDao.listByQuestionId(question.getId());
                JSONArray answersArray = new JSONArray();
                for (Answer answer : answers) {
                    if (answer.getQuestionId() == question.getId()) {
                        JSONObject answerObject = new JSONObject();
                        answerObject.put("answerId", answer.getId());
                        answerObject.put("questionId", answer.getQuestionId());
                        answerObject.put("answerText", answer.getText());
                        answersArray.add(answerObject);
                    }
                }
                questionObject.put("answers", answersArray);
                questionsArray.add(questionObject);
            }
            surveyJson.put("questions", questionsArray);


        return new HttpMessage("HTTP/1.1 200 OK", surveyJson.toJSONString());
    }
}
