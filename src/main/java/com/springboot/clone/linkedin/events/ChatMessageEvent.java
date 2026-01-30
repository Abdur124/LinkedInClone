package com.springboot.clone.linkedin.events;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageEvent {

    private Long conversationId;
    private String messageId;
    private Long userId;
    private String message;
    private Instant createdAt;
}
