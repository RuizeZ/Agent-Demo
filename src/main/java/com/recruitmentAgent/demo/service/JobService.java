package com.recruitmentAgent.demo.service;

import java.util.List;

import com.recruitmentAgent.demo.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.recruitmentAgent.demo.model.Job;

@Service
@Slf4j
public class JobService {
    private final JobMapper jobMapper;

    public JobService(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    public List<Job> searchJobs(String keyword) {
        return jobMapper.searchJobs(keyword);
    }

    public List<Job> findAll() {
        return jobMapper.findAll();
    }
}
