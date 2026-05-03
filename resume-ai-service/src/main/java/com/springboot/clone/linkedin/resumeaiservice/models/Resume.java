package com.springboot.clone.linkedin.resumeaiservice.models;

import lombok.Data;

import java.util.List;

@Data
public class Resume {

    private Basics basics;
    private String summary;
    private List<String> skills;
    private List<Experience> experience;
    private List<Project> projects;
    private List<Education> education;
}
