package no.kristiania.http;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HttpResponse {


    public static void writeOKResponse(String responseBody, String contentType, OutputStream out) throws IOException {
        String responseTxt = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + responseBody.getBytes().length + "\r\n" +
                "Content-Type: " + contentType + "; charset=utf-8\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                responseBody;
        out.write(responseTxt.getBytes(StandardCharsets.UTF_8));
    }

    public static void responseTxt404NotFound( OutputStream out, String requestTarget) throws IOException {
        String responseTxt = "File not found: " + requestTarget;
        String response = "HTTP/1.1 404 Not found\r\n" +
                "Content-Length: " + responseTxt.getBytes().length +"\r\n"+
                "Connection: close\r\n" +
                "\r\n" +
                responseTxt;

        out.write(response.getBytes(StandardCharsets.UTF_8));
    }

}
