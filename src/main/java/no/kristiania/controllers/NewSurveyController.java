package no.kristiania.controllers;

import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.SurveyDao;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.*;


import java.sql.SQLException;
import java.util.Map;

public class NewSurveyController implements HttpController {
    private final SurveyDao surveyDao;
    private final QuestionDao questionDao;
    private final AnswerDao answerDao;



    public NewSurveyController(SurveyDao surveyDao, QuestionDao questionDao, AnswerDao answerDao) {
        this.surveyDao = surveyDao;
        this.questionDao = questionDao;
        this.answerDao = answerDao;

    }

    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpServer.divideRequestMap(request.getMessageBody());

        Survey survey = new Survey();
        survey.setName(queryMap.get("survey-title"));
        survey.setDescription(queryMap.get("survey-description"));
        survey.setUserEmail(queryMap.get("userEmail"));
        surveyDao.save(survey);



        int numberOfQuestions = 0;
        for (String key :
                queryMap.keySet()) {
            if (key.startsWith("q")) {
                int i = Integer.parseInt(String.valueOf(key.charAt(1)));
                if (numberOfQuestions < i) {
                    numberOfQuestions = i;
                }
            }
        }

        for (int i = 1; i <= numberOfQuestions; i++) {
            Question question = new Question();
            question.setSurveyId(survey.getId());
            question.setTitle(queryMap.get("q" + i + "-title"));
            question.setType(queryMap.get("q" + i + "-type"));
            question.setSurveyId(survey.getId());
            questionDao.save(question);

            int numberOfAnswers = 0;
            for (String key :
                    queryMap.keySet()) {
                if (key.charAt(3) == 'a' && key.startsWith("q" + i)) {
                    numberOfAnswers++;
                }
            }

            for (int j = 0; j < numberOfAnswers; j++) {
                Answer answer = new Answer();
                answer.setQuestionId(question.getId());
                if (question.getType().equals("3")) {
                    if (numberOfAnswers > 1) {
                        numberOfAnswers--;
                        answer.setText("min: " + queryMap.get("q" + i + "-a" + j + "-min") + " max: " + queryMap.get("q" + i + "-a" + j + "-max"));
                        answerDao.save(answer);
                    }
                } else if (question.getType().equals("4")) {
                    answer.setText("maxChar: " + queryMap.get("q" + i + "-a" + j + "-text"));
                    answerDao.save(answer);
                } else {
                    answer.setText(queryMap.get("q" + i + "-a" + j + "-choice"));
                    answerDao.save(answer);
                }
            }
        }

        return new HttpMessage("HTTP/1.1 200 OK", "Survey name: " + survey.getName() + "with id: " + survey.getId());
    }
}





