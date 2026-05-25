package com.recruitmentAgent.demo.model;

import lombok.Data;

@Data
public class JobEmbedding {

    private Long id;
    private Long jobId;
    private String content;
    private String embedding;
}
