package com.recruitmentAgent.demo.skill;

import com.recruitmentAgent.demo.model.Job;
import com.recruitmentAgent.demo.rag.RAGService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchJobSkill implements Skill {

    private final RAGService ragService;

    public SearchJobSkill(RAGService ragService) {
        this.ragService = ragService;
    }

    @Override
    public String name() {
        return "search_job";
    }

    @Override
    public String execute(String input) {

        List<Job> jobs = ragService.retrieve(input);

        return jobs.toString();
    }
}
