package com.recruitmentAgent.demo.mapper;

import com.recruitmentAgent.demo.model.Candidate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CandidateMapper {

    @Select("""
            
                SELECT id, name, skills, experience, location
            
                FROM candidate
            
                WHERE skills LIKE CONCAT('%', #{keyword}, '%')
            
                   OR experience LIKE CONCAT('%', #{keyword}, '%')
            
            """)
    List<Candidate> searchCandidates(String keyword);

    @Select("""
            
                SELECT id, name, skills, experience, location
            
                FROM candidate
            
            """)
    List<Candidate> findAll();

}
