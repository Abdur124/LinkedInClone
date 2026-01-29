package com.springboot.clone.linkedin.posts_service.services;

import com.springboot.clone.linkedin.events.PostCreateEvent;
import com.springboot.clone.linkedin.events.PostLikeEvent;
import com.springboot.clone.linkedin.posts_service.entities.Post;
import com.springboot.clone.linkedin.posts_service.entities.PostLike;
import com.springboot.clone.linkedin.posts_service.exceptions.BadRequestException;
import com.springboot.clone.linkedin.posts_service.repos.PostLikesRepository;
import com.springboot.clone.linkedin.posts_service.repos.PostsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikesService {

    private final PostsRepository postsRepository;
    private final PostLikesRepository postLikesRepository;
    private final KafkaTemplate<Long, PostLikeEvent> kafkaTemplate;

    @Value("${kafka.topic.name.postliked}")
    private String kafkaPostLikedTopic;

    @Transactional
    public void likePost(long postId, long userId) {

        Post post = postsRepository.findById(postId).orElseThrow(() ->
                new RuntimeException("Post not found with id: " + postId));

        boolean liked = postLikesRepository.existsByUserIdAndPostId(userId, postId);

        if(liked) {
            throw new BadRequestException("Cannot like the same post again.");
        }

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikesRepository.save(postLike);
        log.info("Post with id: {} liked successfully", postId);

        PostLikeEvent postLikeEvent = PostLikeEvent.builder()
                .postId(postId)
                .likedByUserId(userId)
                .creatorId(post.getUserId())
                .build();

        kafkaTemplate.send(kafkaPostLikedTopic, postId, postLikeEvent);
    }

    @Transactional
    public void unlikePost(long postId, long userId) {

        Post post = postsRepository.findById(postId).orElseThrow(() ->
                new RuntimeException("Post not found with id: " + postId));

        boolean liked = postLikesRepository.existsByUserIdAndPostId(userId, postId);

        if(!liked) {
            throw new BadRequestException("Cannot unlike the same post again.");
        }

        postLikesRepository.deleteByUserIdAndPostId(userId, postId);
        log.info("Post with id: {} unliked successfully", postId);
    }
}
