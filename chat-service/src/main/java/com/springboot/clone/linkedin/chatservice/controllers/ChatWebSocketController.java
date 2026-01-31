package com.springboot.clone.linkedin.chatservice.controllers;

import com.springboot.clone.linkedin.chatservice.dto.ChatMessageRequest;
import com.springboot.clone.linkedin.chatservice.events.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final KafkaTemplate<Long, ChatMessageEvent> kafkaTemplate;

    @MessageMapping({"/chat.send"})
    public void publishMessageSentEvent(ChatMessageRequest request) {
        ChatMessageEvent messageEvent = ChatMessageEvent.builder().conversationId(request.getConversationId()).userId(request.getUserId()).message(request.getMessage()).messageId(UUID.randomUUID().toString()).createdAt(Instant.now()).build();
        log.info("Sending message to {}", messageEvent.getConversationId());
        this.kafkaTemplate.send("chat-messages", messageEvent.getConversationId(), messageEvent);
    }

}
