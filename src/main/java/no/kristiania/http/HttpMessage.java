package no.kristiania.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class HttpMessage {
    private final Map<String, String> header  = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    public String startLine;
    public String messageBody;


    public HttpMessage(Socket socket) throws IOException {
        startLine = HttpMessage.readLine(socket);
        readHeaders(socket);
        if (header.containsKey("Content-Length")) {
            messageBody = HttpMessage.readBytes(socket, getContentLength());
        }
    }

    public HttpMessage(String startLine, String messageBody) {
        this.startLine = startLine;
        this.messageBody = messageBody;
    }


    static String readLine(Socket socket) throws IOException {
        InputStream in = socket.getInputStream();
        StringBuilder buffer = new StringBuilder();
        int c;
        while ((c = in.read()) != '\r') {
            buffer.append((char) c);
        }
        int expectedNewline = socket.getInputStream().read();
        assert expectedNewline == '\n';
        return java.net.URLDecoder.decode(buffer.toString(), StandardCharsets.UTF_8);
    }

    static String readBytes(Socket socket, int contentLength) throws IOException {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            buffer.append((char)socket.getInputStream().read());
        }

        return java.net.URLDecoder.decode(buffer.toString(), StandardCharsets.UTF_8);
    }

    private void readHeaders(Socket socket) throws IOException {
        String responseHeader;
        while (!((responseHeader = HttpMessage.readLine(socket)).isBlank())){
            String[] headerField = responseHeader.split(":");
            header.put(headerField[0].trim(),headerField[1].trim());
        }
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getStartLine() {
        return startLine;
    }

    public String getHeader(String nameOfHeader) {
        return header.get(nameOfHeader);
    }

    public int getContentLength() {
        return Integer.parseInt(getHeader("Content-Length"));
    }

    public void write(Socket socket) throws IOException {
        String responseTxt = startLine + "\r\n" +
                "Content-Length: " + messageBody.getBytes().length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                messageBody;
        socket.getOutputStream().write(responseTxt.getBytes(StandardCharsets.UTF_8));
    }
}
