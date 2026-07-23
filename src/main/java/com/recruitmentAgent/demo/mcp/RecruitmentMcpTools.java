package com.recruitmentAgent.demo.mcp;

import com.recruitmentAgent.demo.model.Candidate;
import com.recruitmentAgent.demo.model.Job;
import com.recruitmentAgent.demo.tool.CandidateSearchTool;
import com.recruitmentAgent.demo.tool.JobSearchTool;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecruitmentMcpTools {

    private final JobSearchTool jobSearchTool;
    private final CandidateSearchTool candidateSearchTool;

    public RecruitmentMcpTools(
            JobSearchTool jobSearchTool,
            CandidateSearchTool candidateSearchTool
    ) {
        this.jobSearchTool = jobSearchTool;
        this.candidateSearchTool = candidateSearchTool;
    }

    @McpTool(
            name = "job_search",
            description = "根据用户的岗位名称、技术方向、工作地点和职位偏好语义检索相关职位",
            generateOutputSchema = true
    )
    public List<Job> searchJobs(
            @McpToolParam(
                    description = "用户的完整职位搜索条件",
                    required = true
            )
            String query
    ) {
        return jobSearchTool.execute(query);
    }

    @McpTool(
            name = "candidate_search",
            description = "根据职位要求、技术栈、经验和地点语义检索合适的候选人",
            generateOutputSchema = true
    )
    public List<Candidate> searchCandidates(
            @McpToolParam(
                    description = "用于匹配候选人的完整职位要求",
                    required = true
            )
            String query
    ) {
        return candidateSearchTool.execute(query);
    }
}
