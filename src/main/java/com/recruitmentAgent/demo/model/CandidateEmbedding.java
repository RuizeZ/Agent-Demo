package com.recruitmentAgent.demo.model;

import lombok.Data;

@Data
public class CandidateEmbedding {

    private Long id;
    private Long candidateId;
    private String content;
    private String embedding;
}
