package com.springboot.clone.linkedin.posts_service.repos;

import com.springboot.clone.linkedin.posts_service.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Post, Long> {

    List<Post> findPostsByUserId(Long userId);
}
