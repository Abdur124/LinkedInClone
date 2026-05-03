package com.springboot.clone.linkedin.resumeaiservice.models;

import lombok.Data;

import java.util.List;

@Data
public class Project {

    private String name;
    private List<String> techStack;
    private String description;
    private List<String> bullets;
}
