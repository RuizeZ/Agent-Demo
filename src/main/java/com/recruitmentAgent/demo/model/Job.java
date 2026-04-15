package com.recruitmentAgent.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Job {

    private String id;          // 职位ID
    private String title;       // 职位名称（Java后端）
    private String description; // 职位描述（技能要求）
}
