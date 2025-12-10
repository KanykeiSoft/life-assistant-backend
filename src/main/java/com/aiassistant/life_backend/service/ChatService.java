package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.model.ChatSession;
import com.aiassistant.life_backend.model.User;
import java.util.List;

public interface ChatService {
    ChatSession createSession(String email);
    List<ChatSession> getMySessions(String email);
}
