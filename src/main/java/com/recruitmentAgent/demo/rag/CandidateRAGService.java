package com.recruitmentAgent.demo.rag;

import com.recruitmentAgent.demo.model.Candidate;
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

    public CandidateRAGService(
            EmbeddingService embeddingService,
            CandidateVectorStore candidateVectorStore,
            CandidateService candidateService
    ) {
        this.embeddingService = embeddingService;
        this.candidateVectorStore = candidateVectorStore;
        this.candidateService = candidateService;
    }

    @PostConstruct
    public void init() {
        List<Candidate> candidates = candidateService.findAll();

        for (Candidate candidate : candidates) {
            String text = buildCandidateText(candidate);
            List<Double> vector = embeddingService.embed(text);
            candidateVectorStore.add(candidate, vector);
        }

        log.info("候选人 RAG 初始化完成，候选人数量：{}", candidates.size());
    }

    public List<Candidate> retrieve(String query) {
        List<Double> queryVector = embeddingService.embed(query);
        return candidateVectorStore.search(queryVector, 2);
    }

    private String buildCandidateText(Candidate candidate) {
        return candidate.getName() + " "
                + candidate.getSkills() + " "
                + candidate.getExperience() + " "
                + candidate.getLocation();
    }
}
