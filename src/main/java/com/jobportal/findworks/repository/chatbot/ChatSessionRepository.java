package com.jobportal.findworks.repository.chatbot;

import com.jobportal.findworks.entity.chatbot.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    Optional<ChatSession> findTop1ByUser_IdOrderByCreatedAtDesc(Long userId);
}