package com.recruitmentAgent.demo.controller;

import com.recruitmentAgent.demo.mcp.McpCallRequest;
import com.recruitmentAgent.demo.tool.JobSearchTool;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mcp")
public class McpToolController {

    private final JobSearchTool jobSearchTool;

    public McpToolController(JobSearchTool jobSearchTool) {
        this.jobSearchTool = jobSearchTool;
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
                )
        );
    }

    @PostMapping("/call")
    public Object call(@RequestBody McpCallRequest request) {

        if ("job_search".equals(request.getTool())) {
            String query = request.getArgs().get("query").toString();
            return jobSearchTool.execute(query);
        }

        return "未知工具：" + request.getTool();
    }

    // 这个可以先保留，方便你调试
    @GetMapping("/job-search")
    public String searchJob(@RequestParam String query) {
        return jobSearchTool.execute(query);
    }
}
