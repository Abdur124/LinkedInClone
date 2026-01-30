package com.springboot.clone.linkedin.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class CreateMessageRequestDto {

    private Long userId;
    private Long targetUserId;
}
