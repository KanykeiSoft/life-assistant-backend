package com.aiassistant.life_backend.controller;

import com.aiassistant.life_backend.dto.ChatRequest;
import com.aiassistant.life_backend.model.ChatSession;
import com.aiassistant.life_backend.service.ChatService;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/sessions")
    public ChatSession createSession(Authentication authentication) {
        String email = authentication.getName();
        return chatService.createSession(email);

    }

    @GetMapping("/sessions")
    public List<ChatSession> getMySessions(Authentication authentication) {
        String email = authentication.getName();
        return chatService.getMySessions(email);
    }

    @PostMapping("/sessions/{sessionId}/message")
    public Map<String, String> sendMessageToSession(
            @PathVariable Long sessionId,
            @RequestBody ChatRequest body,
            Authentication authentication) {
        String email = authentication.getName();
        return chatService.sendMessage(email, sessionId, body.getMessage());
    }

//    @PostMapping("/message")
//    public Map<String, String> sendMessage(
//            @RequestBody ChatRequest body,
//            Authentication authentication) {
//        String email = authentication.getName();
//
//        // если chatSessionId null -> создаём новую сессию
//        Long sessionId = body.getChatSessionId();
//        if (sessionId == null) {
//            sessionId = chatService.createSession(email).getId();
//        }
//
//        return chatService.sendMessage(email, sessionId, body.getMessage());
//    }
}
