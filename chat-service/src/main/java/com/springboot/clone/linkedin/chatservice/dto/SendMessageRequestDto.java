package com.springboot.clone.linkedin.chatservice.dto;

import lombok.Data;

@Data
public class SendMessageRequestDto {

    private Long conversationId;
    private Long senderId;
    private String content;
}
