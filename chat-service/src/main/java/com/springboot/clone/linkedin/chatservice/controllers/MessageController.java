package com.springboot.clone.linkedin.chatservice.controllers;

import com.springboot.clone.linkedin.chatservice.dto.SendMessageRequestDto;
import com.springboot.clone.linkedin.chatservice.entities.Message;
import com.springboot.clone.linkedin.chatservice.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping({"/send"})
    public ResponseEntity<Message> sendMessage(@RequestBody SendMessageRequestDto sendMessageRequestDto) {
        Message message = this.messageService.sendMessage(sendMessageRequestDto.getConversationId(), sendMessageRequestDto.getSenderId(), sendMessageRequestDto.getContent());
        return ResponseEntity.ok(message);
    }

    @GetMapping({"/conversation/{conversationId}"})
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long conversationId, @RequestParam Long userId) {
        List<Message> messages = this.messageService.getMessages(conversationId, userId);
        return ResponseEntity.ok(messages);
    }
}
