package com.recruitmentAgent.demo.skill;

import com.recruitmentAgent.demo.rag.RAGService;
import com.recruitmentAgent.demo.tool.JobSearchTool;
import org.springframework.stereotype.Component;

@Component
public class SearchJobSkill implements Skill {

    private JobSearchTool jobSearchTool;

    public SearchJobSkill(RAGService ragService, JobSearchTool jobSearchTool) {
        this.jobSearchTool = jobSearchTool;
    }

    @Override
    public String name() {
        return "search_job";
    }

    @Override
    public String execute(String input) {

        return jobSearchTool.execute(input);
    }
}
