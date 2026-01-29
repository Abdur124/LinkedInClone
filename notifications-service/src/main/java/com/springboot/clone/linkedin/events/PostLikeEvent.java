package com.springboot.clone.linkedin.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeEvent {

    private Long postId;
    private Long creatorId;
    private Long likedByUserId;
}
