package com.recruitmentAgent.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Candidate {
    private Long id;

    private String name;

    private String skills;

    private String experience;

    private String location;
}
