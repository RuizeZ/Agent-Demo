package com.recruitmentAgent.demo.rag;

import com.recruitmentAgent.demo.mapper.JobEmbeddingMapper;
import com.recruitmentAgent.demo.model.Job;
import com.recruitmentAgent.demo.model.JobEmbedding;
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

    @Autowired
    private JobEmbeddingMapper jobEmbeddingMapper;

    // 初始化：把所有Job变成向量
    @PostConstruct
    public void init() {
        List<Job> all = jobService.findAll();

        for (Job job : all) {
            String text = buildJobText(job);
            List<Double> vector = getOrCreateEmbedding(job, text);
            vectorStore.add(job, vector);
        }
        log.info("RAG 初始化完成，职位数量：{}", all.size());
    }

    // 查询
    public List<Job> retrieve(String query) {
        List<Double> queryVector = embeddingService.embed(query);
        return vectorStore.search(queryVector, 2);
    }

    private List<Double> getOrCreateEmbedding(Job job, String content) {
        JobEmbedding cached = jobEmbeddingMapper.findByJobId(job.getId());

        if (cached != null && content.equals(cached.getContent())) {
            System.out.println("读取职位向量缓存，jobId=" + job.getId());
            return EmbeddingJsonUtil.fromJson(cached.getEmbedding());
        }

        if (cached != null) {
            System.out.println("职位内容变化，刷新向量缓存，jobId=" + job.getId());

            List<Double> vector = embeddingService.embed(content);

            cached.setContent(content);
            cached.setEmbedding(EmbeddingJsonUtil.toJson(vector));

            jobEmbeddingMapper.update(cached);

            return vector;
        }

        System.out.println("生成职位向量，jobId=" + job.getId());

        List<Double> vector = embeddingService.embed(content);

        JobEmbedding jobEmbedding = new JobEmbedding();
        jobEmbedding.setJobId(job.getId());
        jobEmbedding.setContent(content);
        jobEmbedding.setEmbedding(EmbeddingJsonUtil.toJson(vector));

        jobEmbeddingMapper.insert(jobEmbedding);

        return vector;
    }

    private String buildJobText(Job job) {
        return job.getTitle() + " "
                + job.getDescription() + " "
                + job.getRequirement() + " "
                + job.getLocation();
    }
}
