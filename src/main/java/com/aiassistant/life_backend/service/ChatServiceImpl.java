package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.model.ChatSession;
import com.aiassistant.life_backend.model.User;
import com.aiassistant.life_backend.repository.ChatSessionRepository;
import com.aiassistant.life_backend.repository.UserRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService{
    private final ChatSessionRepository sessionRepository;
    private final UserRepository userRepository;

    public ChatServiceImpl(ChatSessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ChatSession createSession(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->new RuntimeException("User not found"));
        ChatSession chatSession = new ChatSession();
        chatSession.setUser(user);
        chatSession.setTitle("New Chat");
        chatSession.setCreatedAt(Instant.now());
        return sessionRepository.save(chatSession);
    }
}
