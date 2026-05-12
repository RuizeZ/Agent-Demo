package com.recruitmentAgent.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Job {
    private Long id;

    private String title;

    private String description;

    private String location;

    private String requirement;
}
