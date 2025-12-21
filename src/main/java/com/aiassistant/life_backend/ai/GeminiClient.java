package com.aiassistant.life_backend.ai;

import com.aiassistant.life_backend.ai.config.GeminiProps;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatusCode;

@Component
public class GeminiClient {

    private final WebClient geminiWebClient;
    private final GeminiProps props;

    public GeminiClient(WebClient geminiWebClient, GeminiProps props) {
        this.geminiWebClient = geminiWebClient;
        this.props = props;
    }

    public String generateText(String prompt) {
        String model = props.getModel();

        System.out.println("--- GEMINI DEBUG ---");
        System.out.println("Base URL: " + props.getBaseUrl());
        System.out.println("Model: " + model);
        System.out.println("API Key present: " +
                (props.getApiKey() != null && !props.getApiKey().isBlank()));
        System.out.println("--------------------");

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(Map.of("text", prompt)))));

        Map<?, ?> response = geminiWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/models/{model}:generateContent")
                        .queryParam("key", props.getApiKey())
                        .build(model))
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(String.class)
                        .flatMap(msg -> Mono.error(new RuntimeException(
                                "Gemini API Error (" + resp.statusCode() + "): " + msg))))
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(props.getTimeoutMs()))
                .block();

        return extractText(response);
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map<?, ?> response) {
        if (response == null)
            return "No response from Gemini";

        try {
            Object candidatesObj = response.get("candidates");
            if (!(candidatesObj instanceof List<?> candidates) || candidates.isEmpty()) {
                return "Empty Gemini response (no candidates)";
            }

            Object firstObj = candidates.get(0);
            if (!(firstObj instanceof Map<?, ?> first)) {
                return "Empty Gemini response (candidate invalid)";
            }

            Object contentObj = first.get("content");
            if (!(contentObj instanceof Map<?, ?> content)) {
                return "Empty Gemini response (no content)";
            }

            Object partsObj = content.get("parts");
            if (!(partsObj instanceof List<?> parts) || parts.isEmpty()) {
                return "Empty Gemini response (no parts)";
            }

            Object part0 = parts.get(0);
            if (!(part0 instanceof Map<?, ?> part)) {
                return "Empty Gemini response (part invalid)";
            }

            Object text = part.get("text");
            return text == null ? "Empty Gemini response (no text)" : text.toString();

        } catch (Exception e) {
            return "Failed to parse Gemini response";
        }
    }
}