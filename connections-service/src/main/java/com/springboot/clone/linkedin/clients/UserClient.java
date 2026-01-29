package com.springboot.clone.linkedin.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/auth")
public interface UserClient {

    @GetMapping("/{userId}/getUserName")
    public String getUserName(@PathVariable Long userId);
}
