package com.recruitmentAgent.demo.controller;

import com.recruitmentAgent.demo.tool.JobSearchTool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp")
public class McpToolController {

    private final JobSearchTool jobSearchTool;

    public McpToolController(JobSearchTool jobSearchTool) {
        this.jobSearchTool = jobSearchTool;
    }

    @GetMapping("/job-search")
    public String searchJob(
            @RequestParam String query
    ) {
        return jobSearchTool.execute(query);
    }
}
