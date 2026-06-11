package com.recruitmentAgent.demo.tool;

import com.recruitmentAgent.demo.model.Job;
import com.recruitmentAgent.demo.rag.RAGService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobSearchTool implements Tool {

    private final RAGService ragService;

    public JobSearchTool(RAGService ragService) {
        this.ragService = ragService;
    }

    @Override
    public String name() {
        return "job_search_tool";
    }

    @Override
    public String execute(String input) {

        List<Job> jobs = ragService.retrieve(input);

        return jobs.toString();
    }
}
