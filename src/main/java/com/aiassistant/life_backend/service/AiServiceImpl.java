package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.ai.GeminiClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
@Service
public class AiServiceImpl implements AiService{
    private final GeminiClient geminiClient;

    public AiServiceImpl(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    @Override
    public String generate(String prompt) {
        if (!StringUtils.hasText(prompt)) {
            return "Prompt is empty.";
        }
        return geminiClient.generateText(prompt);
    }
}
