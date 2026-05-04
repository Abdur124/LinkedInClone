package com.springboot.clone.linkedin.resumeaiservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JDData {

    private List<String> keywords;
    private List<String> expandedKeywords;
}
