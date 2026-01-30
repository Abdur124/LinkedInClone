package com.springboot.clone.linkedin.services;

import com.springboot.clone.linkedin.events.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "chat-messages", groupId = "chat-service-group")
    public void handleChatMessages(ChatMessageEvent chatMessageEvent) {
        log.info("handle chatMessage for user: {} and conversation: {}", chatMessageEvent.getUserId(),
                chatMessageEvent.getConversationId());
        String message =
                "Message from user %d received"+chatMessageEvent.getUserId();

        messagingTemplate.convertAndSend("/topic/conversations/" + chatMessageEvent.getConversationId(),
                chatMessageEvent);
    }


}
