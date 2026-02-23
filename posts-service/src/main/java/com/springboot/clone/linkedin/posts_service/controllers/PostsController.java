package com.springboot.clone.linkedin.posts_service.controllers;

import com.springboot.clone.linkedin.clients.ConnectionsClient;
import com.springboot.clone.linkedin.clients.UserClient;
import com.springboot.clone.linkedin.posts_service.auth.UserContextHolder;
import com.springboot.clone.linkedin.posts_service.dtos.PersonDto;
import com.springboot.clone.linkedin.posts_service.dtos.PostCreateRequestDto;
import com.springboot.clone.linkedin.posts_service.dtos.PostDto;
import com.springboot.clone.linkedin.events.PostCreateEvent;
import com.springboot.clone.linkedin.posts_service.services.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostsController {

    private final PostsService postsService;
    private final ModelMapper modelMapper;
    private final UserClient userClient;
    private final ConnectionsClient connectionsClient;
    private final KafkaTemplate<Long, PostCreateEvent> kafkaTemplate;

    @Value("${kafka.topic.name.postcreated}")
    private String kafkaPostCreatedTopic;

    @GetMapping("/postId/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("postId") Long id) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("userId from token: {}", userId);
        PostDto postDto = postsService.getPostById(id);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostDto>> getPostsByUserId() {
        Long id = UserContextHolder.getCurrentUserId();
        List<PostDto> posts = postsService.getPostsByUserId(id);
        String username = userClient.getUserName(id);
        log.info("username: {}", username);
        List<PersonDto> connections = connectionsClient.getFirstDegreeConnectionsByUserId(username);
        log.info("connectionsSize: {}", connections.size());
        log.info("Connections: {}", connections.toString());
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        PostDto createdPost = postsService.createPost(postCreateRequestDto, userId);
        PostCreateEvent postCreateEvent = PostCreateEvent.builder().
                postId(createdPost.getId()).
                userId(userId).
                content(createdPost.getContent()).
                build();
        kafkaTemplate.send(kafkaPostCreatedTopic, postCreateEvent);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }
}
