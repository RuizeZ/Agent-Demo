package com.recruitmentAgent.demo.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentAgent.demo.mcp.ToolExecutor;
import com.recruitmentAgent.demo.service.QwenService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgentService {
    @Autowired
    private QwenService qwenService;

    @Autowired
    private ToolExecutor toolExecutor;

    private ObjectMapper objectMapper = new ObjectMapper();

    public String handle(String userInput) {
        String context = userInput; // 👉 初始上下文

        try {
            for (int i = 0; i < 3; i++) { // 👉 最多执行3步（防死循环）

                // 1️⃣ 调AI（带上下文）
                String aiResponse = qwenService.call(context);

                // 👉 处理可能的 ```json
                aiResponse = aiResponse.replace("```json", "").replace("```", "");

                JsonNode node = objectMapper.readTree(aiResponse);

                // 👉 如果AI说“结束”
                if (node.has("final_answer")) {
                    return node.get("final_answer").asText();
                }

                // 2️⃣ 获取tool
                String tool = node.get("tool").asText();
                JsonNode args = node.get("args");

                // 3️⃣ 执行Tool
                Object result = toolExecutor.execute(tool, args);

                // 4️⃣ 把结果喂回AI（关键！！！）
                context = context + "\n" + tool + "工具调用结果：" + result;

            }

            return "执行结束（达到最大步骤）";

        } catch (Exception e) {
            e.printStackTrace();
            return "系统错误";
        }
    }
}
