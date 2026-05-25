package com.recruitmentAgent.demo.rag;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class EmbeddingJsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(List<Double> vector) {
        try {
            return objectMapper.writeValueAsString(vector);
        } catch (Exception e) {
            throw new RuntimeException("向量转 JSON 失败", e);
        }
    }

    public static List<Double> fromJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<Double>>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON 转向量失败", e);
        }
    }
}
