package com.recruitmentAgent.demo.mcp;

import com.recruitmentAgent.demo.service.CandidateService;
import com.recruitmentAgent.demo.service.JobService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ToolExecutor {

    @Autowired
    private JobService jobService;

    @Autowired
    private CandidateService candidateService;

    public Object execute(String toolName, JsonNode args) {

        switch (toolName) {

            case "search_jobs":
                String keyword = args.get("keyword").asText();
                return jobService.searchJobs(keyword);

            case "match_candidates":
                String desc = args.get("jobDesc").asText();
                return candidateService.matchCandidates(desc);

            default:
                return "未知工具";
        }
    }
}