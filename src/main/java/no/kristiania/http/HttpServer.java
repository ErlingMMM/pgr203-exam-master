package no.kristiania.http;

import no.kristiania.controllers.HttpController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class HttpServer {


    public static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private final ServerSocket serverSocket;
    private final Map<String, HttpController> controllers = new HashMap<>();

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        new Thread(this::handleConnections).start();
    }



    private void handleConnections() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                OutputStream out = socket.getOutputStream();
                try {
                    String[] requestLine = HttpMessage.readLine(socket).split(" ");
                    String requestTarget = requestLine[1];
                    if (requestTarget.equals("/")) requestTarget = "/index.html";
                    handleFileTarget(requestTarget, out, socket);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleFileTarget(String requestTarget, OutputStream out, Socket socket) throws IOException, SQLException {
        HttpMessage httpMessage = new HttpMessage(socket);
        int questionPosition = requestTarget.indexOf("?");
        String fileTarget;
        String queryInUrl = null;
        if (questionPosition != -1) {
            fileTarget = requestTarget.substring(0, questionPosition);
            queryInUrl = requestTarget.substring(questionPosition + 1);

        } else {
            fileTarget = requestTarget;
        }

        if (controllers.containsKey(fileTarget)) {
            if (queryInUrl != null) {
                httpMessage.messageBody = queryInUrl;
            }
            HttpMessage response = controllers.get(fileTarget).handle(httpMessage);
            response.write(socket);
            return;
        }
        InputStream fileResource = getClass().getResourceAsStream(fileTarget);
        if (fileResource != null) {
            checkContentTypeAndWriteOKResponse(fileTarget, out, fileResource);
            return;
        }

        HttpResponse.responseTxt404NotFound(out, requestTarget);
    }


    private void checkContentTypeAndWriteOKResponse(String requestTarget, OutputStream out, InputStream fileResource) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        fileResource.transferTo(buffer);
        String responseBody = buffer.toString();

        String contentType = "text/plain";
        if (requestTarget.endsWith(".html")) {
            contentType = "text/html";
        } else if (requestTarget.endsWith(".css")) {
            contentType = "text/css";
        } else if (requestTarget.endsWith(".ico")) {
            contentType = "image/x-icon";
        }
        HttpResponse.writeOKResponse(responseBody, contentType, out);
    }


    public static Map<String, String> divideRequestMap(String queryInUrl) {
        Map<String, String> queryMap = new HashMap<>();
        if (queryInUrl.contains("=")) {
            for (String queryParameter : queryInUrl.split("&")) {
                int equalsPosition = queryParameter.indexOf('=');
                String parameterName = URLDecoder.decode(queryParameter.substring(0, equalsPosition), StandardCharsets.UTF_8);
                String parameterValue = URLDecoder.decode(queryParameter.substring(equalsPosition + 1), StandardCharsets.UTF_8);
                queryMap.put(parameterName, parameterValue);
            }
        } else if (queryInUrl.contains(":") && queryInUrl.contains("{")) {
            int openingBracketPosition = queryInUrl.indexOf('{');
            int closingBracketPosition = queryInUrl.indexOf('}');
            String query = queryInUrl.substring(openingBracketPosition + 1, closingBracketPosition).replaceAll("\"", "");
            for (String queryParameter : query.split(",")) {
                int colonPosition = queryParameter.indexOf(':');
                String parameterName = URLDecoder.decode(queryParameter.substring(0, colonPosition), StandardCharsets.UTF_8);
                String parameterValue = URLDecoder.decode(queryParameter.substring(colonPosition + 1), StandardCharsets.UTF_8);
                queryMap.put(parameterName, parameterValue);
            }
        }
        return queryMap;
    }


    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void addController(String path, HttpController controller) {
        controllers.put(path, controller);
    }
}
