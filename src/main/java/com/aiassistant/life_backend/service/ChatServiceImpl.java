package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.model.ChatSession;
import com.aiassistant.life_backend.model.User;
import com.aiassistant.life_backend.repository.ChatSessionRepository;
import com.aiassistant.life_backend.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final AiService aiService;

    public ChatServiceImpl(ChatSessionRepository sessionRepository, UserRepository userRepository, AiService aiService) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.aiService = aiService;
    }

    @Override
    public ChatSession createSession(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ChatSession chatSession = new ChatSession();
        chatSession.setUser(user);
        chatSession.setTitle("New Chat");
        chatSession.setCreatedAt(Instant.now());
        return sessionRepository.save(chatSession);
    }

    @Override
    public List<ChatSession> getMySessions(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return sessionRepository.findByUserId(user.getId());

    }

    @Override
    public Map<String, String> sendMessage(String email, Long sessionId, String prompt) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // ✅ защита: сессия должна принадлежать пользователю
        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden: not your session");
        }

        String answer = aiService.generate(prompt);

        // TODO позже: сохранить user message + ai message в БД

        return Map.of("answer", answer);
    }
}