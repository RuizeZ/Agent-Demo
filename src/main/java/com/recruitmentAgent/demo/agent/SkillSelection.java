package com.recruitmentAgent.demo.agent;

import lombok.Data;

@Data
public class SkillSelection {

    /**
     * 千问选择的 Skill 名称。
     */
    private String skill;

    /**
     * 传给 Skill.execute() 的输入。
     */
    private String input;
}
