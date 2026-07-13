package com.recruitmentAgent.demo.controller;

import com.recruitmentAgent.demo.mcp.McpCallRequest;
import com.recruitmentAgent.demo.tool.CandidateSearchTool;
import com.recruitmentAgent.demo.tool.JobSearchTool;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mcp")
public class McpToolController {

    private final JobSearchTool jobSearchTool;

    private final CandidateSearchTool candidateSearchTool;

    public McpToolController(JobSearchTool jobSearchTool, CandidateSearchTool candidateSearchTool) {
        this.jobSearchTool = jobSearchTool;
        this.candidateSearchTool = candidateSearchTool;
    }

    @GetMapping("/tools")
    public List<Map<String, Object>> tools() {
        return List.of(
                Map.of(
                        "name", "job_search",
                        "description", "根据用户输入搜索相关职位",
                        "input", Map.of(
                                "query", "string"
                        )
                ),
                Map.of(
                        "name", "candidate_search",
                        "description", "根据职位要求语义检索合适的候选人",
                        "input", Map.of(
                                "query", "string"
                        )
                )
        );
    }

    @PostMapping("/call")
    public Object call(@RequestBody McpCallRequest request) {

        if ("job_search".equals(request.getTool())) {
            String query = getQuery(request);
            return jobSearchTool.execute(query);
        }

        if ("candidate_search".equals(request.getTool())) {
            String query = getQuery(request);
            return candidateSearchTool.execute(query);
        }

        return "未知工具：" + request.getTool();
    }

    private String getQuery(McpCallRequest request) {
        Object query = request.getArgs().get("query");

        if (query == null) {
            throw new IllegalArgumentException("缺少必要参数：query");
        }

        return query.toString();
    }

    // 这个可以先保留，方便你调试
    @GetMapping("/job-search")
    public Object searchJob(@RequestParam String query) {
        return jobSearchTool.execute(query);
    }
}
