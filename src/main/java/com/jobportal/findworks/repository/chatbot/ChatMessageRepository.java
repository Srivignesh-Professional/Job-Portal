package com.jobportal.findworks.repository.chatbot;

import com.jobportal.findworks.entity.chatbot.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}