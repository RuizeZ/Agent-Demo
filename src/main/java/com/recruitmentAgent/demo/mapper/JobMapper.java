package com.recruitmentAgent.demo.mapper;

import com.recruitmentAgent.demo.model.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobMapper {

    @Select("""
            
                SELECT id, title, description, location, requirement
            
                FROM job
            
                WHERE title LIKE CONCAT('%', #{keyword}, '%')
            
                   OR description LIKE CONCAT('%', #{keyword}, '%')
            
                   OR requirement LIKE CONCAT('%', #{keyword}, '%')
            
            """)
    List<Job> searchJobs(String keyword);

    @Select("""
            
                SELECT id, title, description, location, requirement
            
                FROM job
            
            """)
    List<Job> findAll();

}
