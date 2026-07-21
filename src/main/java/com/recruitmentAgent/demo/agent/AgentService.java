package com.recruitmentAgent.demo.agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentAgent.demo.mcp.ToolExecutor;
import com.recruitmentAgent.demo.model.Job;
import com.recruitmentAgent.demo.rag.CandidateRAGService;
import com.recruitmentAgent.demo.rag.RAGService;
import com.recruitmentAgent.demo.service.QwenService;
import com.recruitmentAgent.demo.skill.RecommendCandidateSkill;
import com.recruitmentAgent.demo.skill.SearchJobSkill;
import com.recruitmentAgent.demo.skill.Skill;
import com.recruitmentAgent.demo.skill.SkillRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AgentService {
    private final SkillRouterService skillRouterService;
    private final SkillRegistry skillRegistry;

    public AgentService(SkillRouterService skillRouterService, SkillRegistry skillRegistry) {
        this.skillRouterService = skillRouterService;
        this.skillRegistry = skillRegistry;
    }

    public String handle(String userInput) {
        // 1. 让千问选择 Skill
        SkillSelection selection =
                skillRouterService.selectSkill(userInput);

        System.out.println(
                "Agent 选择 Skill："
                        + selection.getSkill()
        );

        System.out.println(
                "Skill 输入："
                        + selection.getInput()
        );

        // 2. 根据名称从 Registry 中取得 Skill
        Skill skill =
                skillRegistry.getSkill(selection.getSkill());

        // 3. 执行 Skill
        return skill.execute(selection.getInput());
    }
}
