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
    public String description() {
        return "根据用户输入的技术方向、岗位名称、工作地点等条件搜索相关职位";
    }

    @Override
    public String execute(String input) {
        return mcpClient.searchJob(input);
    }
}
