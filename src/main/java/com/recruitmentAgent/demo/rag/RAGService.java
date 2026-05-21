package com.recruitmentAgent.demo.rag;

import com.recruitmentAgent.demo.model.Job;
import com.recruitmentAgent.demo.service.JobService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RAGService {

    @Autowired
    private EmbeddingService embeddingService;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private JobService jobService;

    // 初始化：把所有Job变成向量
    @PostConstruct
    public void init() {
        List<Job> all = jobService.findAll();

        for (Job job : all) {
            String text = buildJobText(job);
            List<Double> vector = embeddingService.embed(text);
            vectorStore.add(job, vector);
        }
        log.info("RAG 初始化完成，职位数量：{}", all.size());
    }

    // 查询
    public List<Job> retrieve(String query) {
        List<Double> queryVector = embeddingService.embed(query);
        return vectorStore.search(queryVector, 2);
    }

    private String buildJobText(Job job) {
        return job.getTitle() + " "
                + job.getDescription() + " "
                + job.getRequirement() + " "
                + job.getLocation();
    }
}
