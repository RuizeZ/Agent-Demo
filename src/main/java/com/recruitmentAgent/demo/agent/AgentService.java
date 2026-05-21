package com.recruitmentAgent.demo.agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentAgent.demo.mcp.ToolExecutor;
import com.recruitmentAgent.demo.model.Job;
import com.recruitmentAgent.demo.rag.CandidateRAGService;
import com.recruitmentAgent.demo.rag.RAGService;
import com.recruitmentAgent.demo.service.QwenService;
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

    public String handle(String userInput) {

        // 1. 根据用户输入检索职位
        var relatedJobs = ragService.retrieve(userInput);

        // 2. 把职位信息拼成候选人检索 query
        String jobText = relatedJobs.toString();

        // 3. 根据职位要求检索候选人
        var relatedCandidates = candidateRAGService.retrieve(jobText);

        // 4. 构造给大模型的上下文
        String prompt = """
            你是一个招聘助手。

            用户需求：
            %s

            检索到的相关职位：
            %s

            检索到的相关候选人：
            %s

            请你根据以上信息，给出：
            1. 推荐的职位
            2. 推荐的候选人
            3. 推荐理由

            不要编造不存在的信息。
            """.formatted(userInput, relatedJobs, relatedCandidates);

        // 5. 让千问总结
        return qwenService.call(prompt);
    }
}
