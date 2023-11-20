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

public class EditSurveyController implements HttpController{
    private SurveyDao surveyDao;
    private QuestionDao questionDao;
    private AnswerDao answerDao;

    public EditSurveyController(SurveyDao surveyDao, QuestionDao questionDao, AnswerDao answerDao) {
        this.surveyDao = surveyDao;
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        JSONObject surveyJson = new JSONObject();
        Map<String, String> queryMap = HttpServer.divideRequestMap(request.getMessageBody());
        long surveyId = Long.parseLong(queryMap.get("id"));

        Survey survey = surveyDao.retrieve(surveyId);
        surveyJson.put("surveyId", survey.getId());
        surveyJson.put("surveyName", survey.getName());
        surveyJson.put("surveyDescription", survey.getDescription());
        List<Question> questionList = questionDao.listAllBySurveyId(surveyId);
        JSONArray questionArray = new JSONArray();
        for (Question question : questionList) {
            JSONObject questionJson = new JSONObject();
            questionJson.put("questionId", question.getId());
            questionJson.put("surveyId", question.getSurveyId());
            questionJson.put("questionTitle", question.getTitle());
            questionJson.put("questionType", question.getType());
            List<Answer> answerList = answerDao.listByQuestionId(question.getId());
            JSONArray questionAnswerArray = new JSONArray();
            for (Answer answer : answerList) {
                JSONObject answerJson = new JSONObject();
                answerJson.put("answerId", answer.getId());
                answerJson.put("questionId", answer.getQuestionId());
                answerJson.put("answerText", answer.getText());
                questionAnswerArray.add(answerJson);
            }
            questionJson.put("answers", questionAnswerArray);
            questionArray.add(questionJson);
        }
        surveyJson.put("questions", questionArray);

        return new HttpMessage("HTTP/1.1 200 OK",surveyJson.toJSONString());
    }
}
