package com.recruitmentAgent.demo.skill;

public interface Skill {

    /**
     * Skill 的唯一名称。
     * Agent 会根据这个名字找到具体 Skill。
     */
    String name();

    /**
     * 给大模型看的能力说明。
     */
    String description();

    /**
     * 执行业务能力。
     */
    String execute(String input);
}
