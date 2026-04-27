package com.recruitmentAgent.demo.rag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.recruitmentAgent.demo.model.Job;

@Component
public class VectorStore {

    private List<Job> jobs = new ArrayList<>();

    private List<List<Double>> vectors = new ArrayList<>();

    public void add(Job job, List<Double> embedding) {

        jobs.add(job);

        vectors.add(embedding);

    }

    public List<Job> search(List<Double> queryVector, int topK) {

        List<Map.Entry<Job, Double>> scored = new ArrayList<>();

        for (int i = 0; i < vectors.size(); i++) {

            double sim = SimilarityUtil.cosineSimilarity(queryVector, vectors.get(i));

            scored.add(Map.entry(jobs.get(i), sim));

        }

        return scored.stream()

                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))

                .limit(topK)

                .map(Map.Entry::getKey)

                .toList();

    }

}
