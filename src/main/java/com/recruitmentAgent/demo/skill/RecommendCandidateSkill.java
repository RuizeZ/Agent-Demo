package com.recruitmentAgent.demo.skill;

import com.recruitmentAgent.demo.model.Candidate;
import com.recruitmentAgent.demo.rag.CandidateRAGService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecommendCandidateSkill implements Skill {

    private final CandidateRAGService candidateRAGService;

    public RecommendCandidateSkill(
            CandidateRAGService candidateRAGService
    ) {
        this.candidateRAGService = candidateRAGService;
    }

    @Override
    public String name() {
        return "recommend_candidate";
    }

    @Override
    public String execute(String input) {

        List<Candidate> candidates =
                candidateRAGService.retrieve(input);

        return candidates.toString();
    }
}
