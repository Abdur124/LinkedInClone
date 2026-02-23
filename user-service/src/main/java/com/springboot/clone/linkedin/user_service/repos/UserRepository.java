package com.springboot.clone.linkedin.user_service.repos;

import com.springboot.clone.linkedin.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
