package com.recruitmentAgent.demo.tool;

import com.recruitmentAgent.demo.model.Candidate;
import com.recruitmentAgent.demo.model.Job;
import com.recruitmentAgent.demo.rag.RAGService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobSearchTool implements Tool<List<Job>> {

    private final RAGService ragService;

    public JobSearchTool(RAGService ragService) {
        this.ragService = ragService;
    }

    @Override
    public String name() {
        return "job_search_tool";
    }

    @Override
    public List<Job> execute(String input) {

         return ragService.retrieve(input);

    }
}
