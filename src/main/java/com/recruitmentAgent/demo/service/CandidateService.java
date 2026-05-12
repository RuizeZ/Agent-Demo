package com.recruitmentAgent.demo.service;

import java.util.List;

import com.recruitmentAgent.demo.mapper.CandidateMapper;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.recruitmentAgent.demo.model.Candidate;

@Service
@Slf4j
public class CandidateService {

    private final CandidateMapper candidateMapper;

    public CandidateService(CandidateMapper candidateMapper) {
        this.candidateMapper = candidateMapper;
    }

    public List<Candidate> matchCandidates(String jobDesc) {
        return candidateMapper.searchCandidates(jobDesc);
    }

    public List<Candidate> findAll() {
        return candidateMapper.findAll();
    }
}
