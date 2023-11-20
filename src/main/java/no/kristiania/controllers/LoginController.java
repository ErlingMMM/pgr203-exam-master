package no.kristiania.controllers;

import no.kristiania.dao.UserDao;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;



public class LoginController implements HttpController {
    private final UserDao userDao;

    public LoginController(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpServer.divideRequestMap(request.messageBody);
        String userInput = (queryMap.get("userInputMail"));

        User user = userDao.retrieveByEmail(userInput.toLowerCase(Locale.ROOT) );
        JSONArray jsonArray = new JSONArray();

            JSONObject jsonObjectUser = new JSONObject();
        jsonObjectUser.put("logInId", user.getId());
        jsonObjectUser.put("logInFirstName", user.getFirstName());
        jsonObjectUser.put("logInLastName", user.getLastName());
        jsonObjectUser.put("logInMailOutput", user.getEmail());
            jsonArray.add(jsonObjectUser);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", jsonArray);


        return new HttpMessage("HTTP/1.1 200 OK", jsonObject.toJSONString());
    }
}
