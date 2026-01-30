package com.springboot.clone.linkedin.controllers;


import com.springboot.clone.linkedin.dto.CreateMessageRequestDto;
import com.springboot.clone.linkedin.entities.Conversation;
import com.springboot.clone.linkedin.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping("/dm")
    public ResponseEntity<Conversation> getOrCreateDM(@RequestBody CreateMessageRequestDto createMessageRequestDto) {
        Conversation conversation =
                conversationService.getOrCreateDM(createMessageRequestDto.getUserId(),
                        createMessageRequestDto.getTargetUserId());
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conversation>> getUserConversations(@PathVariable Long userId) {
        List<Conversation> conversations =
                conversationService.getUserConversations(userId);
        return ResponseEntity.ok(conversations);
    }
}

