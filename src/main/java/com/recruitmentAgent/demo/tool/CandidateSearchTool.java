package com.recruitmentAgent.demo.tool;


import com.recruitmentAgent.demo.model.Candidate;
import com.recruitmentAgent.demo.rag.CandidateRAGService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CandidateSearchTool implements Tool {

    private final CandidateRAGService candidateRAGService;

    public CandidateSearchTool(
            CandidateRAGService candidateRAGService
    ) {
        this.candidateRAGService = candidateRAGService;
    }

    @Override
    public String name() {
        return "candidate_search_tool";
    }

    @Override
    public String execute(String input) {

        List<Candidate> candidates =
                candidateRAGService.retrieve(input);

        return candidates.toString();
    }
}
