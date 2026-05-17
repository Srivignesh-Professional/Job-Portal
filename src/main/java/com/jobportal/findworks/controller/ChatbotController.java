package com.jobportal.findworks.controller;

import com.jobportal.findworks.dto.chatbot.ChatRequest;
import com.jobportal.findworks.dto.chatbot.ChatResponse;
import com.jobportal.findworks.security.model.UserPrincipal;
import com.jobportal.findworks.service.impl.chatbot.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/message")
    public ChatResponse message(@AuthenticationPrincipal UserPrincipal principal,
                                @RequestBody ChatRequest req) {
        String reply = chatbotService.handleMessage(principal.getUser().getId(), req.getMessage());
        return new ChatResponse(reply);
    }
}
