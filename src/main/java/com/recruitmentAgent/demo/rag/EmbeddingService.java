package com.recruitmentAgent.demo.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;

import java.util.*;

@Service
public class EmbeddingService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingService.class);

    @Value("${qwen.apiKey:}")
    private String API_KEY;

    private final String URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/embeddings";

    private RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings({ "rawtypes" })
    public List<Double> embed(String text) {

        if (text == null || text.isBlank()) {
            log.warn("Embedding requested with blank text; returning empty embedding.");
            return List.of();
        }

        if (API_KEY == null || API_KEY.isBlank()) {
            log.error(
                    "qwen.apiKey is empty; cannot call embedding API. Set env DASHSCOPE_API_KEY or property qwen.apiKey.");
            return List.of();
        }

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(API_KEY);

        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();

        body.put("model", "text-embedding-v4");

        body.put("input", text);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            long startNs = System.nanoTime();
            ResponseEntity<Map> response = restTemplate.postForEntity(URL, request, Map.class);
            log.info("response is: {}", response.getBody());
            long elapsedMs = (System.nanoTime() - startNs) / 1_000_000;

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Embedding API returned non-2xx status={} elapsedMs={}", response.getStatusCode(), elapsedMs);
                return List.of();
            }

            Map bodyMap = response.getBody();
            if (bodyMap == null) {
                log.error("Embedding API returned empty body. status={} elapsedMs={}", response.getStatusCode(),
                        elapsedMs);
                return List.of();
            }

            List data = (List) bodyMap.get("data");
            if (data == null) {
                log.error("Embedding API response missing 'data'. keys={} elapsedMs={}", bodyMap.keySet(), elapsedMs);
                return List.of();
            }

            Map embeddingObject = (Map) data.get(0);
            if (embeddingObject == null || embeddingObject.isEmpty()) {
                log.error("Embedding API response missing/empty 'data.embedding'. keys={} elapsedMs={}", embeddingObject.keySet(), elapsedMs);
                return List.of();
            }

            List embedding = (List) embeddingObject.get("embedding");
            if (embedding == null || embedding.isEmpty()) {
                log.error("Embedding API response missing/empty 'embedding'. embeddingKeys={} elapsedMs={}",
                embeddingObject.keySet(), elapsedMs);
                return List.of();
            }

            // 最小处理：把 Number 转成 Double，避免运行时强转 List<Double> 失败
            List<Double> result = new ArrayList<>(embedding.size());
            for (Object v : embedding) {
                if (v instanceof Number n) {
                    result.add(n.doubleValue());
                }
            }

            log.info("Embedding generated dims={} elapsedMs={}", result.size(), elapsedMs);
            return result;

        } catch (RestClientResponseException e) {
            log.error("Embedding API call failed status={} responseBody={}",
                    e.getStatusCode().value(), e.getResponseBodyAsString(), e);
            return List.of();
        } catch (Exception e) {

            log.error("Embedding API call failed.", e);

            return List.of();

        }

    }

}
