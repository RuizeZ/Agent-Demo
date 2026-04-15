package com.recruitmentAgent.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Candidate {

    private String id;     // 候选人ID
    private String name;   // 姓名
    private String skills; // 技能（Java / Python）
}
