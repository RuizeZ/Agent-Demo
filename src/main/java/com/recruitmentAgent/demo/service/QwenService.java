package com.recruitmentAgent.demo.service;

import org.springframework.web.client.RestTemplate;

import com.recruitmentAgent.demo.mcp.ToolDefinition;
import com.recruitmentAgent.demo.mcp.ToolRegistry;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.springframework.http.HttpEntity;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QwenService {

  @Autowired
  private ToolRegistry toolRegistry;

  private static final String URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

  @Value("${qwen.apiKey:}")
  private String apiKey;

  private RestTemplate restTemplate = new RestTemplate();

  public String call(String userInput) {
    long start = System.currentTimeMillis();
    if (apiKey == null || apiKey.isBlank()) {
      throw new IllegalStateException("Missing config: qwen.apiKey (set env DASHSCOPE_API_KEY)");
    }

    // 👉 构造 Prompt（极其关键）
    String prompt = buildPrompt(userInput, toolRegistry.getTools());
    log.info("qwen.call.start prompt={}", prompt);

    // 👉 请求头
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + apiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    // 👉 请求体
    Map<String, Object> body = new HashMap<>();
    body.put("model", "qwen3.5-122b-a10b");

    body.put("messages", new Object[] {
        Map.of("role", "system", "content", "你是一个招聘AI助手"),
        Map.of("role", "user", "content", prompt)
    });

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

    // 👉 调用AI
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> response = restTemplate.postForEntity(URL, request, Map.class);
    log.info("qwen.call.http status={} costMs={}", response.getStatusCode(), System.currentTimeMillis() - start);
    log.info("qwen.call.http body={}", response.getBody());
    // 👉 解析返回
    String content = parseResponse(response.getBody());
    log.info("qwen.call.done contentLen={} preview={}", content == null ? 0 : content.length(), preview(content));
    return content;
  }

  private String parseResponse(Map<?, ?> response) {
    try {
      List<?> choices = (List<?>) response.get("choices");
      Map<?, ?> choice = (Map<?, ?>) choices.get(0);
      Map<?, ?> message = (Map<?, ?>) choice.get("message");
      return message.get("content").toString();
    } catch (Exception e) {
      log.warn("qwen.parseResponse.fail err={}", e.toString());
      return "{}";
    }
  }

  // 🔥 核心：让AI输出 Tool JSON
  private String buildPrompt(String userInput, List<ToolDefinition> tools) {
    StringBuilder toolDesc = new StringBuilder();
    for (ToolDefinition tool : tools) {
      toolDesc.append("工具名: ").append(tool.getName()).append("\n")
          .append("描述: ").append(tool.getDescription()).append("\n")
          .append("参数: ").append(tool.getParameters()).append("\n\n");
    }

    return """
        你是一个AI Agent，可以调用工具完成任务。
        主要流程：
        1. 每一步只能调用一个工具
        2. 如果任务未完成，继续调用工具
        3. 如果任务完成，无需调用工具，返回：
          {
            "final_answer": "结果"
          }
        重要规则（必须遵守）：
        - 如果用户输入中包含一段“相关职位：[...]”，并且方括号内不是空列表（不是 []）
          则说明系统已经为你检索到了最相关的职位。此时如果你选择 search_jobs，
          keyword 必须取“相关职位”列表中第一条 Job 的 title（例如 Java后端），
          不要用用户问题中的泛化词（例如 微服务、后端、开发等）。
        - 如果“相关职位：[]”为空列表，才允许你从用户问题中提取 keyword。
        - 只能返回 JSON，禁止输出任何解释性文字。

        可用工具如下：
        """ + toolDesc + """

        用户输入：
        """ + userInput + """

        如果需要调用工具，请你选择最合适的工具，并返回JSON：
        {
          "tool": "工具名",
          "args": {
            "参数名": "参数值"
          }
        }

        只返回JSON，不要解释。
        """;
  }

  private String preview(String s) {
    if (s == null)
      return "null";
    return s.replace("\n", "\\n").replace("\r", "\\r");
  }
}
