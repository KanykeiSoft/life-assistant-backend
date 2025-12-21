package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.model.ChatSession;
import com.aiassistant.life_backend.model.User;
import java.util.List;
import java.util.Map;

public interface ChatService {
    ChatSession createSession(String email);
    List<ChatSession> getMySessions(String email);
    Map<String, String> sendMessage(String email, Long sessionId, String prompt);
}
