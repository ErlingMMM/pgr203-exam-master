package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpServer;
import no.kristiania.survey.User;
import no.kristiania.dao.UserDao;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.Map;

public class CreateUserController implements HttpController {
    private final UserDao userDao;

    public CreateUserController(UserDao userDao) {
        this.userDao = userDao;
    }


    public HttpMessage handle(HttpMessage request) throws SQLException {
        try {
            Map<String, String> queryMap = HttpServer.divideRequestMap(request.messageBody);
            User user = new User();
            user.setFirstName(queryMap.get("firstName"));
            user.setLastName(queryMap.get("lastName"));
            user.setEmail(queryMap.get("email"));
            userDao.save(user);
            return new HttpMessage("HTTP/1.1 200 OK", "Success");

        } catch (PSQLException e) {
            System.out.println("Already registered email. This sql raw is set to unique");
            return new HttpMessage("400 bad request", "Already registered email. This sql raw is set to unique");
        }
    }
}