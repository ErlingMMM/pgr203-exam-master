package no.kristiania.controllers;


import no.kristiania.dao.UserDao;
import no.kristiania.http.HttpMessage;

import no.kristiania.survey.Survey;
import no.kristiania.dao.SurveyDao;


import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


public class ListSurveyController implements HttpController {

    private final SurveyDao surveyDao;
    private final UserDao userDao;

    public ListSurveyController(SurveyDao surveyDao, UserDao userDao) {
        this.surveyDao = surveyDao;
        this.userDao = userDao;
    }


    boolean isSortAsc = false;
    boolean isButtonClicked = false;

    private List<Survey> chooseSortingOnClick() throws SQLException {
        isSortAsc = !isSortAsc;
        if (isButtonClicked) {
            if (isSortAsc) return surveyDao.listAllDescending();
            else return surveyDao.listAllAscending();
        } else {
            isButtonClicked = true;
            return surveyDao.listAll();
        }
    }


    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {

        StringBuilder responseTxt = new StringBuilder();


        List<Survey> unmodifiableList = Collections.unmodifiableList(chooseSortingOnClick());
        for (Survey survey : unmodifiableList) {
            responseTxt.append("<div onClick=\"takeSurvey(")
                    .append(survey.getId())
                    .append(")\"  class=\"survey\">Name of survey: <br> ")
                    .append(survey.getName())
                    .append("<br><br> the topic for this survey is: ")
                    .append(survey.getDescription())
                    .append("<br><br> This survey was created by:<br>").append(userDao.retrieveByEmail(survey.getUserEmail()).getFirstName()).append(", ")
                    .append(userDao.retrieveByEmail(survey.getUserEmail()).getLastName())
                    .append("<br><br> Contact:<br>")
                    .append(survey.getUserEmail())
                    .append("</div>")
                    .append("<div onClick=\"editSurvey(")
                    .append(survey.getId())
                    .append(")\"class=\"survey\"><p>Edit this survey</p>")
                    .append("</div>")
                    .append("<div class=\"see-answers-btn\" onClick=\"seeSurveyAnswers(")
                    .append(survey.getId())
                    .append(")\"><p>See all answers for this survey</p></div><br>");
        }
        return new HttpMessage("HTTP/1.1 200 OK", (java.net.URLDecoder.decode(responseTxt.toString(), StandardCharsets.UTF_8)));
    }
}

