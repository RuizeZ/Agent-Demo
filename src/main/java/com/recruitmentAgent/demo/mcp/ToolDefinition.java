package com.recruitmentAgent.demo.mcp;

import java.util.Map;

public class ToolDefinition {

    private String name;
    private String description;
    private Map<String, String> parameters;

    public ToolDefinition(String name, String description, Map<String, String> parameters) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
