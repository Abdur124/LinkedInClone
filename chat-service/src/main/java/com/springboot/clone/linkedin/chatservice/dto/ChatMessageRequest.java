package com.springboot.clone.linkedin.chatservice.dto;

import lombok.Data;

@Data
public class ChatMessageRequest {

    private Long conversationId;
    private Long userId;
    private String message;
}
