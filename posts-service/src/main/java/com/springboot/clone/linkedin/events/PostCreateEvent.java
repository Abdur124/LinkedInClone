package com.springboot.clone.linkedin.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreateEvent {

    private Long postId;
    private String content;
    private Long userId;
}
