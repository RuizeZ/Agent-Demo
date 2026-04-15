package com.recruitmentAgent.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recruitmentAgent.demo.model.Job;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping
    public String test() {
        return "hello";
    }

    @GetMapping("/testJob")
    public Job testJob() {
        return new Job("1", "Java后端", "Spring Boot");
    }
}
