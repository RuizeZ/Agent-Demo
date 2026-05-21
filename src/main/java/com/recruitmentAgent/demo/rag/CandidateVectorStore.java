package com.recruitmentAgent.demo.rag;

import com.recruitmentAgent.demo.model.Candidate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CandidateVectorStore {

    private final List<Candidate> candidates = new ArrayList<>();
    private final List<List<Double>> vectors = new ArrayList<>();

    public void add(Candidate candidate, List<Double> embedding) {
        candidates.add(candidate);
        vectors.add(embedding);
    }

    public List<Candidate> search(List<Double> queryVector, int topK) {
        List<Map.Entry<Candidate, Double>> scored = new ArrayList<>();

        for (int i = 0; i < vectors.size(); i++) {
            double score = SimilarityUtil.cosineSimilarity(queryVector, vectors.get(i));
            scored.add(Map.entry(candidates.get(i), score));
        }

        return scored.stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(topK)
                .map(Map.Entry::getKey)
                .toList();
    }
}
