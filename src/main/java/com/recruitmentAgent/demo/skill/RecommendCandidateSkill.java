package com.recruitmentAgent.demo.skill;

import com.recruitmentAgent.demo.tool.CandidateSearchTool;
import org.springframework.stereotype.Component;

@Component
public class RecommendCandidateSkill implements Skill {


    private final CandidateSearchTool candidateSearchTool;

    public RecommendCandidateSkill(CandidateSearchTool candidateSearchTool) {
        this.candidateSearchTool = candidateSearchTool;
    }

    @Override
    public String name() {
        return "recommend_candidate";
    }

    @Override
    public String execute(String input) {

        return candidateSearchTool.execute(input);
    }
}
