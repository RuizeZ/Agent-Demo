package com.recruitmentAgent.demo.skill;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class SkillRegistry {

    private final Map<String, Skill> skillMap;

    /**
     * Spring 会自动把所有实现了 Skill 接口的 Bean 注入进来。
     * <p>
     * 当前会注入：
     * 1. SearchJobSkill
     * 2. RecommendCandidateSkill
     */
    public SkillRegistry(List<Skill> skills) {
        this.skillMap = new LinkedHashMap<>();

        for (Skill skill : skills) {
            if (skillMap.containsKey(skill.name())) {
                throw new IllegalStateException(
                        "发现重复的 Skill 名称：" + skill.name()
                );
            }

            skillMap.put(skill.name(), skill);
        }
    }

    /**
     * 返回所有可用 Skill。
     */
    public List<Skill> listSkills() {
        return List.copyOf(skillMap.values());
    }

    /**
     * 根据名称取得具体 Skill。
     */
    public Skill getSkill(String name) {
        Skill skill = skillMap.get(name);

        if (skill == null) {
            throw new IllegalArgumentException(
                    "不存在的 Skill：" + name
            );
        }

        return skill;
    }
}
