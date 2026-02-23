package com.springboot.clone.linkedin.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLikeEvent {

    private Long postId;
    private Long creatorId;
    private Long likedByUserId;
}
