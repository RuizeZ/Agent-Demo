package com.recruitmentAgent.demo.mcp;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ToolRegistry {

    public List<ToolDefinition> getTools() {

        return List.of(
                new ToolDefinition(
                        "search_jobs",
                        "根据关键词搜索职位",
                        Map.of("keyword", "string")
                ),
                new ToolDefinition(
                        "match_candidates",
                        "根据职位描述匹配候选人",
                        Map.of("jobDesc", "string")
                )
        );
    }
}
