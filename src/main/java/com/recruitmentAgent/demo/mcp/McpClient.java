package com.recruitmentAgent.demo.mcp;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class McpClient {

    private final RestTemplate restTemplate =
            new RestTemplate();

    public String searchJob(String query) {

        String url =
                "http://localhost:8080/mcp/job-search?query="
                        + query;

        return restTemplate.getForObject(
                url,
                String.class
        );
    }
}
