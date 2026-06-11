package com.recruitmentAgent.demo.skill;

import com.recruitmentAgent.demo.mcp.McpClient;
import org.springframework.stereotype.Component;

@Component
public class SearchJobSkill implements Skill {

    private final McpClient mcpClient;

    public SearchJobSkill(McpClient mcpClient) {
        this.mcpClient = mcpClient;
    }

    @Override
    public String name() {
        return "search_job";
    }

    @Override
    public String execute(String input) {

        return mcpClient.searchJob(input);
    }
}
