package com.aiassistant.life_backend.repository;

import com.aiassistant.life_backend.model.ChatSession;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    List<ChatSession> findByUserId(Long userId);
}
