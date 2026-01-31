package com.springboot.clone.linkedin.chatservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEvent {

    private Long conversationId;
    private String messageId;
    private Long userId;
    private String message;
    private Instant createdAt;
}
