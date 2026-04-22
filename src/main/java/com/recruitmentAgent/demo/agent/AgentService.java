package com.recruitmentAgent.demo.agent;

import com.recruitmentAgent.demo.mcp.ToolExecutor;
import com.recruitmentAgent.demo.model.Job;
import com.recruitmentAgent.demo.rag.RAGService;
import com.recruitmentAgent.demo.service.CandidateService;
import com.recruitmentAgent.demo.service.JobService;
import com.recruitmentAgent.demo.service.QwenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgentService {
    @Autowired
    private QwenService qwenService;

    @Autowired
    private JobService jobService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private ToolExecutor toolExecutor;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RAGService ragService;

    public String handle(String userInput) {

        try {
            long start = System.currentTimeMillis();
            log.info("agent.handle.start userInput={}", userInput);
            // 1. 调用RAG服务
            List<Job> ragList = ragService.retrieve(userInput);
            ;
            String enhancedInput = "用户问题：" + userInput + "\n相关职位：" + ragList;
            // 1️⃣ 调AI
            String aiResponse = qwenService.call(enhancedInput);
            log.info("agent.handle.aiResponse receivedLen={} preview={}",
                    aiResponse == null ? 0 : aiResponse.length(), preview(aiResponse));

            // 2️⃣ 解析JSON
            JsonNode node;
            try {
                node = parseToolJson(aiResponse);
                log.info("agent.handle.parse.ok extractedToolJsonPreview={}", preview(node.toString()));
            } catch (JsonProcessingException e) {
                log.warn("agent.handle.parse.fail err={} aiPreview={}", e.getOriginalMessage(), preview(aiResponse));
                throw e;
            }

            JsonNode toolNode = node.get("tool");
            if (toolNode == null || toolNode.isNull()) {
                return "AI返回结果不是有效的工具JSON";
            }
            String tool = toolNode.asText();
            log.info("agent.handle.tool tool={}", tool);

            // 3️⃣ 调用对应Tool
            tool = node.get("tool").asText();
            JsonNode args = node.get("args");

            Object result = toolExecutor.execute(tool, args);

            return result.toString();

        } catch (Exception e) {
            log.error("agent.handle.error err={}", e.toString());
            return "系统错误";
        }
    }

    private JsonNode parseToolJson(String aiResponse) throws JsonProcessingException {
        try {
            return objectMapper.readTree(aiResponse);
        } catch (JsonProcessingException ignored) {
            String extracted = extractFirstJsonObject(aiResponse);
            if (extracted == null) {
                throw ignored;
            }
            return objectMapper.readTree(extracted);
        }
    }

    private String extractFirstJsonObject(String s) {
        if (s == null)
            return null;
        int start = s.indexOf('{');
        int end = s.lastIndexOf('}');
        if (start < 0 || end < 0 || end <= start)
            return null;
        return s.substring(start, end + 1);
    }

    private String preview(String s) {
        if (s == null)
            return "null";
        String oneLine = s.replace("\n", "\\n").replace("\r", "\\r");
        int max = 120;
        if (oneLine.length() <= max)
            return oneLine;
        return oneLine.substring(0, max) + "...";
    }
}
