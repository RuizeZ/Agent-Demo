package com.recruitmentAgent.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.recruitmentAgent.demo.model.Candidate;

@Service
@Slf4j
public class CandidateService {
    private List<Candidate> candidates = List.of(
            new Candidate("1", "张三", "Java Spring"),
            new Candidate("2", "李四", "Python AI"),
            new Candidate("3", "王五", "React"));

    // 👉 Tool：根据职位描述匹配候选人
    public List<Candidate> matchCandidates(String jobDesc) {
        String[] jobDescArr = jobDesc.split(" ");
        List<Candidate> result = candidates.stream()
                .filter(c -> {
                    for (String desc : jobDescArr){
                        if (c.getSkills().contains(desc)){
                            return true;
                        }
                    }
                    return false;
                })
                .toList();
        log.info("tool.match_candidates jobDescLen={} hits={}", jobDesc == null ? 0 : jobDesc.length(), result.size());
        return result;
    }

}
