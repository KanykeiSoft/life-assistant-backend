package com.aiassistant.life_backend.controller;

import com.aiassistant.life_backend.model.ChatSession;
import com.aiassistant.life_backend.service.ChatService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/sessions")
    public ChatSession createSession(Authentication authentication){
        String email = authentication.getName();
        return chatService.createSession(email);

    }
    @GetMapping("/sessions")
    public List<ChatSession> getMySessions (Authentication authentication){
        String email = authentication.getName();
        return chatService.getMySessions(email);
    }
}
