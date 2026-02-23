package com.springboot.clone.linkedin.posts_service.controllers;

import com.springboot.clone.linkedin.events.PostCreateEvent;
import com.springboot.clone.linkedin.events.PostLikeEvent;
import com.springboot.clone.linkedin.posts_service.auth.UserContextHolder;
import com.springboot.clone.linkedin.posts_service.entities.Post;
import com.springboot.clone.linkedin.posts_service.exceptions.ResourceNotFoundException;
import com.springboot.clone.linkedin.posts_service.repos.PostsRepository;
import com.springboot.clone.linkedin.posts_service.services.PostLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.nio.file.ReadOnlyFileSystemException;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final PostLikesService postLikesService;
    private final PostsRepository postsRepository;
    private final KafkaTemplate<Long, PostLikeEvent> kafkaTemplate;

    @Value("${kafka.topic.name.postliked}")
    private String kafkaPostLikedTopic;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = postsRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + postId + " not found"));
        postLikesService.likePost(postId, userId);
        PostLikeEvent postLikeEvent = PostLikeEvent.builder()
                .postId(postId)
                .likedByUserId(userId)
                .creatorId(post.getUserId())
                .build();
        kafkaTemplate.send(kafkaPostLikedTopic, postLikeEvent);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        postLikesService.unlikePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

}
