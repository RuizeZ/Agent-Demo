package com.recruitmentAgent.demo.mapper;


import com.recruitmentAgent.demo.model.CandidateEmbedding;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CandidateEmbeddingMapper {

    @Select("""
        SELECT id, candidate_id, content, embedding
        FROM candidate_embedding
        WHERE candidate_id = #{candidateId}
    """)
    CandidateEmbedding findByCandidateId(Long candidateId);

    @Insert("""
        INSERT INTO candidate_embedding (candidate_id, content, embedding)
        VALUES (#{candidateId}, #{content}, #{embedding})
    """)
    void insert(CandidateEmbedding candidateEmbedding);
}
