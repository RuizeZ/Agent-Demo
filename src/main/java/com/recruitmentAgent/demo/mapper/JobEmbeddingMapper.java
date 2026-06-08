package com.recruitmentAgent.demo.mapper;

import com.recruitmentAgent.demo.model.JobEmbedding;
import org.apache.ibatis.annotations.*;

@Mapper
public interface JobEmbeddingMapper {

    @Select("""
        SELECT id, job_id, content, embedding
        FROM job_embedding
        WHERE job_id = #{jobId}
    """)
    JobEmbedding findByJobId(Long jobId);

    @Insert("""
        INSERT INTO job_embedding (job_id, content, embedding)
        VALUES (#{jobId}, #{content}, #{embedding})
    """)
    void insert(JobEmbedding jobEmbedding);

    @Update("""
    UPDATE job_embedding
    SET content = #{content},
        embedding = #{embedding}
    WHERE job_id = #{jobId}
""")
    void update(JobEmbedding jobEmbedding);
}
