package com.recruitmentAgent.demo.rag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recruitmentAgent.demo.model.Job;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RAGService {

    @Autowired
    private EmbeddingService embeddingService;

    @Autowired
    private VectorStore vectorStore;

    private List<Job> jobs = List.of(
            new Job("1", "Java后端", "Spring Boot 微服务 MySQL"),
            new Job("2", "AI工程师", "大模型 RAG NLP"),
            new Job("3", "前端工程师", "React Vue"));

    // 初始化：把所有Job变成向量
    @PostConstruct
    public void init() {

        for (Job job : jobs) {

            String text = job.getTitle() + " " + job.getDescription();

            List<Double> vector = embeddingService.embed(text);

            vectorStore.add(job, vector);

        }

    }

    // 查询
    public List<Job> retrieve(String query) {

        List<Double> queryVector = embeddingService.embed(query);

        return vectorStore.search(queryVector, 2);

    }
}
