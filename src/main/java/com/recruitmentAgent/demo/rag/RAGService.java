package com.recruitmentAgent.demo.rag;

import com.recruitmentAgent.demo.model.Job;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RAGService {

    private List<Job> jobs = List.of(
            new Job("1", "Java后端", "Spring Boot 微服务 MySQL"),
            new Job("2", "AI工程师", "大模型 RAG NLP"),
            new Job("3", "前端工程师", "React Vue"));

    public List<Job> retrieve(String query) {
        List<Job> result = jobs.stream()
                        .filter(job -> match(job, query))
                        .collect(Collectors.toList());
        log.info("rag.retrieve.done result={}", result);
        return result;
    }

    // 👉 简化语义匹配（重点理解）
    private boolean match(Job job, String query) {
        String text = job.getTitle() + job.getDescription();
        if (query.contains("后端") && text.contains("Java"))
            return true;
        if (query.contains("AI") && text.contains("大模型"))
            return true;

        String[] split = job.getDescription().split(" ");
        return Arrays.stream(split).anyMatch(query::contains);
    }
}
