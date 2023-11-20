package no.kristiania.controllers;

import no.kristiania.http.HttpMessage;

import java.sql.SQLException;

public interface HttpController {
    HttpMessage handle(HttpMessage request) throws SQLException;
}
