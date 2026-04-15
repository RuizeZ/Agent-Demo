package com.recruitmentAgent.demo.controller;

import com.recruitmentAgent.demo.agent.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recruitmentAgent.demo.model.Job;
import com.recruitmentAgent.demo.model.Candidate;
import com.recruitmentAgent.demo.service.CandidateService;
import com.recruitmentAgent.demo.service.JobService;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {
    private final AgentService agentService;

    @Autowired
    private JobService jobService;

    @Autowired
    private CandidateService candidateService;

    ApiController(AgentService agentService) {
        this.agentService = agentService;
    }

    // 👉 测试职位查询
    @GetMapping("/jobs")
    public List<Job> getJobs(@RequestParam String keyword) {
        return jobService.searchJobs(keyword);
    }

    // 👉 测试候选人匹配
    @GetMapping("/candidates")
    public List<Candidate> getCandidates(@RequestParam String desc) {
        return candidateService.matchCandidates(desc);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String msg) {
        String requestId = UUID.randomUUID().toString();
        long start = System.currentTimeMillis();
        log.info("chat.request requestId={} msgLen={}", requestId, msg == null ? 0 : msg.length());
        try {
            String result = agentService.handle(msg);
            log.info("chat.response requestId={} costMs={} resultLen={}", requestId, System.currentTimeMillis() - start,
                    result == null ? 0 : result.length());
            return result;
        } catch (RuntimeException e) {
            log.error("chat.error requestId={} costMs={} err={}", requestId, System.currentTimeMillis() - start,
                    e.toString());
            throw e;
        }
    }
}
