package com.springboot.clone.linkedin.dto;

import lombok.Data;

@Data
public class ChatMessageRequest {
    private Long conversationId;
    private Long userId;
    private String message;
}

