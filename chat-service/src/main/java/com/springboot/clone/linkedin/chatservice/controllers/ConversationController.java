package com.springboot.clone.linkedin.chatservice.controllers;

import com.springboot.clone.linkedin.chatservice.dto.CreateMessageRequestDto;
import com.springboot.clone.linkedin.chatservice.entities.Conversation;
import com.springboot.clone.linkedin.chatservice.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping({"/dm"})
    public ResponseEntity<Conversation> getOrCreateDM(@RequestBody CreateMessageRequestDto createMessageRequestDto) {
        Conversation conversation = this.conversationService.getOrCreateDM(createMessageRequestDto.getUserId(), createMessageRequestDto.getTargetUserId());
        return ResponseEntity.ok(conversation);
    }

    @GetMapping({"/user/{userId}"})
    public ResponseEntity<List<Conversation>> getUserConversations(@PathVariable Long userId) {
        List<Conversation> conversations = this.conversationService.getUserConversations(userId);
        return ResponseEntity.ok(conversations);
    }
}
