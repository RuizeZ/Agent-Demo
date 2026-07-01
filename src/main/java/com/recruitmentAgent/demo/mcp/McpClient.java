package com.recruitmentAgent.demo.mcp;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class McpClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String MCP_BASE_URL = "http://localhost:8080/mcp";

    public List<Map<String, Object>> listTools() {
        return restTemplate.getForObject(
                MCP_BASE_URL + "/tools",
                List.class
        );
    }

    public Object callTool(String tool, Map<String, Object> args) {
        McpCallRequest request = new McpCallRequest();
        request.setTool(tool);
        request.setArgs(args);

        return restTemplate.postForObject(
                MCP_BASE_URL + "/call",
                request,
                Object.class
        );
    }

    public String searchJob(String query) {
        Object result = callTool(
                "job_search",
                Map.of("query", query)
        );

        return result.toString();
    }
}
