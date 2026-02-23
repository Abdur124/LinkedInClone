package com.springboot.clone.linkedin.posts_service.repos;

import com.springboot.clone.linkedin.posts_service.entities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikesRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserIdAndPostId(long userId, long postId);

    void deleteByUserIdAndPostId(Long userId, Long postId);
}
