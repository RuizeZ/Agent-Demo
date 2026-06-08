package com.recruitmentAgent.demo.rag;

import com.recruitmentAgent.demo.mapper.CandidateEmbeddingMapper;
import com.recruitmentAgent.demo.model.Candidate;
import com.recruitmentAgent.demo.model.CandidateEmbedding;
import com.recruitmentAgent.demo.service.CandidateService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CandidateRAGService {

    private final EmbeddingService embeddingService;
    private final CandidateVectorStore candidateVectorStore;
    private final CandidateService candidateService;
    private final CandidateEmbeddingMapper candidateEmbeddingMapper;

    public CandidateRAGService(
            EmbeddingService embeddingService,
            CandidateVectorStore candidateVectorStore,
            CandidateService candidateService,
            CandidateEmbeddingMapper candidateEmbeddingMapper
    ) {
        this.embeddingService = embeddingService;
        this.candidateVectorStore = candidateVectorStore;
        this.candidateService = candidateService;
        this.candidateEmbeddingMapper = candidateEmbeddingMapper;
    }

    @PostConstruct
    public void init() {
        List<Candidate> candidates = candidateService.findAll();

        for (Candidate candidate : candidates) {
            String text = buildCandidateText(candidate);
            List<Double> vector = getOrCreateEmbedding(candidate, text);
            candidateVectorStore.add(candidate, vector);
        }

        log.info("候选人 RAG 初始化完成，候选人数量：{}", candidates.size());
    }

    public List<Candidate> retrieve(String query) {
        List<Double> queryVector = embeddingService.embed(query);
        return candidateVectorStore.search(queryVector, 2);
    }

    private List<Double> getOrCreateEmbedding(Candidate candidate, String content) {
        CandidateEmbedding cached = candidateEmbeddingMapper.findByCandidateId(candidate.getId());

        if (cached != null && content.equals(cached.getContent())) {
            System.out.println("读取候选人向量缓存，candidateId=" + candidate.getId());
            return EmbeddingJsonUtil.fromJson(cached.getEmbedding());
        }

        if (cached != null) {
            System.out.println("候选人内容变化，刷新向量缓存，candidateId=" + candidate.getId());

            List<Double> vector = embeddingService.embed(content);

            cached.setContent(content);
            cached.setEmbedding(EmbeddingJsonUtil.toJson(vector));

            candidateEmbeddingMapper.update(cached);

            return vector;
        }

        System.out.println("生成候选人向量，candidateId=" + candidate.getId());

        List<Double> vector = embeddingService.embed(content);

        CandidateEmbedding candidateEmbedding = new CandidateEmbedding();
        candidateEmbedding.setCandidateId(candidate.getId());
        candidateEmbedding.setContent(content);
        candidateEmbedding.setEmbedding(EmbeddingJsonUtil.toJson(vector));

        candidateEmbeddingMapper.insert(candidateEmbedding);

        return vector;
    }

    private String buildCandidateText(Candidate candidate) {
        return candidate.getName() + " "
                + candidate.getSkills() + " "
                + candidate.getExperience() + " "
                + candidate.getLocation();
    }
}
