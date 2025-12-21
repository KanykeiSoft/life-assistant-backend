package com.aiassistant.life_backend.ai;

import com.aiassistant.life_backend.ai.config.GeminiProps;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
public class GeminiClientTest {
    private MockWebServer server;

    @BeforeEach
    void start() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterEach
    void stop() throws IOException {
        server.shutdown();
    }

    @Test
    void generateText_shouldParseAnswerText() {
        // Fake Gemini response
        String json = """
        {
          "candidates": [
            {
              "content": {
                "parts": [
                  { "text": "Hello from mock Gemini" }
                ]
              }
            }
          ]
        }
        """;

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json));

        GeminiProps props = new GeminiProps();
        props.setBaseUrl(server.url("/").toString()); // points to mock server
        props.setApiKey("fake-key");
        props.setModel("gemini-2.5-flash");
        props.setTimeoutMs(3000);

        WebClient webClient = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        GeminiClient client = new GeminiClient(webClient, props);

        String result = client.generateText("Hi");

        assertEquals("Hello from mock Gemini", result);
    }

    @Test
    void generateText_shouldThrowOnHttpError() {
        String errorJson = """
        { "error": { "message": "bad stuff" } }
        """;

        server.enqueue(new MockResponse()
                .setResponseCode(404)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(errorJson));

        GeminiProps props = new GeminiProps();
        props.setBaseUrl(server.url("/").toString());
        props.setApiKey("fake-key");
        props.setModel("gemini-2.5-flash");
        props.setTimeoutMs(3000);

        WebClient webClient = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        GeminiClient client = new GeminiClient(webClient, props);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> client.generateText("Hi"));

        assertTrue(ex.getMessage().contains("Gemini API Error"));
    }
}
