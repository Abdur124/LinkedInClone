package com.springboot.clone.linkedin.dto;

import lombok.Data;

@Data
public class SendMessageRequestDto {

    private Long conversationId;
    private Long senderId;
    private String content;
}
