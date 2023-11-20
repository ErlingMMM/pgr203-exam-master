package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;

import no.kristiania.dao.QuestionTypeDao;


import java.sql.SQLException;

public class QuestionTypeController implements HttpController {

    private final QuestionTypeDao questionTypeDao;

    public QuestionTypeController(QuestionTypeDao questionTypeDao) {
        this.questionTypeDao = questionTypeDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {

        StringBuilder responseTxt = new StringBuilder();

        int value = 1;

        for (String type : questionTypeDao.listAll())
            responseTxt.append(("<option value=")).append(value++).append(">").append(type).append("</option>");

        return new HttpMessage("HTTP/1.1 200 OK", responseTxt.toString());
    }
}


