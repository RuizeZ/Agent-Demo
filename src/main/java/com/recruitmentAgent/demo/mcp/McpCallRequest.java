package com.recruitmentAgent.demo.mcp;

import lombok.Data;

import java.util.Map;

@Data
public class McpCallRequest {
    private String tool;

    private Map<String, Object> args;
}
