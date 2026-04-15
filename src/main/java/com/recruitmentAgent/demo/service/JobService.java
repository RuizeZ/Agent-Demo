package com.recruitmentAgent.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.recruitmentAgent.demo.model.Job;

@Service
@Slf4j
public class JobService {
    // 👉 模拟数据库（现在先写死）
    private List<Job> jobs = List.of(
            new Job("1", "Java后端", "Spring Boot + MySQL"),
            new Job("2", "AI工程师", "LLM + RAG"),
            new Job("3", "前端工程师", "React"));

    // 👉 Tool：根据关键词查职位
    public List<Job> searchJobs(String keyword) {
        List<Job> result = jobs.stream()
                .filter(job -> job.getTitle().contains(keyword))
                .toList();
        log.info("tool.search_jobs keywordLen={} hits={}", keyword == null ? 0 : keyword.length(), result.size());
        return result;
    }
}
