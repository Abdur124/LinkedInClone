package com.springboot.clone.linkedin.posts_service.services;

import com.springboot.clone.linkedin.posts_service.dtos.PostCreateRequestDto;
import com.springboot.clone.linkedin.posts_service.dtos.PostDto;
import com.springboot.clone.linkedin.posts_service.entities.Post;
import com.springboot.clone.linkedin.posts_service.repos.PostsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;

    public PostDto getPostById(Long id) {
        log.info("Getting post by id: " + id);
        Post post = postsRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found for id: " + id));
        return modelMapper.map(post, PostDto.class);
    }


    public List<PostDto> getPostsByUserId(Long userId) {
        log.info("Getting posts by userId: " + userId);
        List<Post> posts = postsRepository.findPostsByUserId(userId);
        return posts.stream().map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, long userId) {
        log.info("Saving post created by userId: " + userId);
        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        return modelMapper.map(postsRepository.save(post), PostDto.class);
    }
}
