package com.recruitmentAgent.demo.skill;

import com.recruitmentAgent.demo.mcp.McpClient;
import com.recruitmentAgent.demo.tool.CandidateSearchTool;
import org.springframework.stereotype.Component;

@Component
public class RecommendCandidateSkill implements Skill {

    private final McpClient mcpClient;

    public RecommendCandidateSkill(McpClient mcpClient) {
        this.mcpClient = mcpClient;
    }

    @Override
    public String name() {
        return "recommend_candidate";
    }

    @Override
    public String description() {
        return "根据职位要求、技术栈、经验和地点等条件推荐合适的候选人";
    }

    @Override
    public String execute(String input) {
        return mcpClient.searchCandidate(input);
    }
}
