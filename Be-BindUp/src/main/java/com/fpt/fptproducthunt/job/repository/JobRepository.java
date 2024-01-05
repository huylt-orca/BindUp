package com.fpt.fptproducthunt.job.repository;

import com.fpt.fptproducthunt.common.entity.Job;
import com.fpt.fptproducthunt.common.entity.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
    //    @Query(value = "Select * from Changelog ", nativeQuery = true)
    List<Job> findAllByProjectId(UUID projectId);

    void deleteById(UUID id);
    //    @Query(value = "Select * from Project where status != 2", nativeQuery = true)
    Page<Job> findAllByProjectId(UUID projectId, PageRequest pageRequest);

    @Query(value = "select * from job where name like %:keyword% and job_status = 0", nativeQuery = true)
    Page<Job> findAll(@Param("keyword") String keyword, PageRequest pageable);
}
