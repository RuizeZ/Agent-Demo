package com.recruitmentAgent.demo.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.springframework.http.HttpEntity;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QwenService {
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
    String prompt = buildPrompt(userInput);
    log.info("qwen.call.start inputLen={} promptLen={}", userInput == null ? 0 : userInput.length(), prompt.length());

    // 👉 请求头
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + apiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    // 👉 请求体
    Map<String, Object> body = new HashMap<>();
    body.put("model", "tongyi-xiaomi-analysis-flash");

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
  private String buildPrompt(String userInput) {
    return """
        你是一个招聘系统AI，你必须从以下工具中选择一个，并返回JSON：

        工具：
        1. search_jobs: 根据关键词搜索职位
           参数：keyword

        2. match_candidates: 根据用户输入的职位描述，匹配候选人
           参数：jobDesc

        用户输入：
        """ + userInput + """

        请严格返回如下格式JSON，不要解释：
        {
          "tool": "工具名",
          "args": {
            "参数名": "参数值"
          }
        }
        """;
  }

  private String preview(String s) {
    if (s == null) return "null";
    return s.replace("\n", "\\n").replace("\r", "\\r");
  }
}
