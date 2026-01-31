package com.springboot.clone.linkedin.chatservice.dto;

import lombok.Data;

@Data
public class CreateMessageRequestDto {

    private Long userId;
    private Long targetUserId;
}
