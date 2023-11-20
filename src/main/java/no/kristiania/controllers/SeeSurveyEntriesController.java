package no.kristiania.controllers;

import no.kristiania.dao.UserSurveyDao;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.UserSurvey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.Map;

public class SeeSurveyEntriesController implements HttpController{

    private final UserSurveyDao userSurveyDao;

    public SeeSurveyEntriesController(UserSurveyDao userSurveyDao){
        this.userSurveyDao = userSurveyDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {

        JSONObject json = new JSONObject();
        Map<String, String> queryMap = HttpServer.divideRequestMap(request.getMessageBody());
        long surveyId = Long.parseLong(queryMap.get("id"));

        json.put("surveyId", surveyId);


        JSONArray entries = new JSONArray();
        for (UserSurvey entry : userSurveyDao.listAllBySurveyId(surveyId)) {
            JSONObject userSurvey = new JSONObject();
            userSurvey.put("id", entry.getId());
            userSurvey.put("createdAt", entry.getCreated_at().toString());
            userSurvey.put("userEmail", entry.getUserEmail());
            entries.add(userSurvey);
        }
        json.put("entries", entries);

        return new HttpMessage("HTTP/1.1 200 OK", json.toJSONString());
    }
}
