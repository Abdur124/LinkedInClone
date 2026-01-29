package com.springboot.clone.linkedin.posts_service.dtos;

public class PersonDto {

    private String userId;

    private String name;

    private String role;

    @Override
    public String toString() {
        return "PersonDto{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
