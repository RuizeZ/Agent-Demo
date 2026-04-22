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

        可用工具如下：
        """ + toolDesc + """

        用户输入：
        """ + userInput + """

        请你选择最合适的工具，并返回JSON：
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
