package no.kristiania.controllers;


import no.kristiania.dao.SurveyDao;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.Survey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FilterSurveyController implements HttpController {

    private final SurveyDao surveyDao;

    public FilterSurveyController( SurveyDao surveyDao) {
        this.surveyDao = surveyDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpServer.divideRequestMap(request.messageBody);
        String userInput = (queryMap.get("filterInput"));

        List<Survey> result = surveyDao.listAllWhereInputLike(userInput.toLowerCase(Locale.ROOT));
        JSONArray jsonArray = new JSONArray();
        for (Survey survey : result) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", survey.getId());
            jsonObject.put("description", survey.getDescription());
            jsonObject.put("name", survey.getName());
            jsonObject.put("userEmail", survey.getUserEmail());
            jsonArray.add(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("surveys", jsonArray);

        return new HttpMessage("HTTP/1.1 200 OK", jsonObject.toJSONString());
    }
}
