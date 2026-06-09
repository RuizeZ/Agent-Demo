package com.recruitmentAgent.demo.agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentAgent.demo.mcp.ToolExecutor;
import com.recruitmentAgent.demo.model.Job;
import com.recruitmentAgent.demo.rag.CandidateRAGService;
import com.recruitmentAgent.demo.rag.RAGService;
import com.recruitmentAgent.demo.service.QwenService;
import com.recruitmentAgent.demo.skill.RecommendCandidateSkill;
import com.recruitmentAgent.demo.skill.SearchJobSkill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AgentService {
    @Autowired
    private QwenService qwenService;

    @Autowired
    private RAGService ragService;

    @Autowired
    private CandidateRAGService candidateRAGService;

    @Autowired
    private SearchJobSkill searchJobSkill;

    @Autowired
    private RecommendCandidateSkill recommendCandidateSkill;

    public String handle(String userInput) {
        if (userInput.contains("职位")
                || userInput.contains("工作")
                || userInput.contains("岗位")) {

            return searchJobSkill.execute(userInput);
        }

        if (userInput.contains("候选人")
                || userInput.contains("推荐")) {

            return recommendCandidateSkill.execute(userInput);
        }

        return "无法识别用户意图";
    }
}
