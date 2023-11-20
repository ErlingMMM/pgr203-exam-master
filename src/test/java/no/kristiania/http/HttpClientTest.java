package no.kristiania.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpClientTest {
    @Test
    public void shouldReturnStatusCode200OK() throws IOException {
        assertEquals(200,
                new HttpClient("httpbin.org", 80, "/html")
                        .getStatusCode());
        assertEquals(404,
                new HttpClient("httpbin.org", 80, "/not-existing-page")
                        .getStatusCode());
    }

    @Test
    public void shouldReturnHTMLContent() throws IOException {
        HttpClient client = new HttpClient("httpbin.org",80,"/html");

        assertTrue(client.getMessageBody().contains("Moby-Dick"));
    }

    @Test
    public void shouldReturnResponseHeaders() throws IOException {
        HttpClient client = new HttpClient("httpbin.org",80,"/html");

        assertEquals("text/html; charset=utf-8", client.getHeader("Content-Type"));
    }
}
