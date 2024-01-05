package com.fpt.fptproducthunt.project.repository;

import com.fpt.fptproducthunt.common.entity.Mentor;
import com.fpt.fptproducthunt.common.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findAllByFounderId(UUID founderId);
    List<Project> findByNameContainingIgnoreCase(String name);;
    @Query(value = "select * from project where name like %:name% and milestone in (:milestones)", nativeQuery = true)
    Page<Project> findUsingFilter(@Param("name") String name, PageRequest paging, @Param("milestones") int[] milestoneType);
    @Query(value = "select * from project where status = :status and name like %:name% and milestone in (:milestones)", nativeQuery = true)
    Page<Project> findUsingFilter(@Param("name") String name, @Param("status") int status, PageRequest paging, @Param("milestones") int[] milestoneType);
    @Query(value = "select * from project where status = :status", nativeQuery = true)
    Page<Project> findAll(PageRequest paging, @Param("status") int status);
    @Query(value = "select * from project", nativeQuery = true)
    Page<Project> findAll(PageRequest paging);

    @Query(value = "select * from vote where projectid = :projectId and userid = :userId", nativeQuery = true)
    Object findVote(@Param("projectId") String projectId, @Param("userId") String userId);
}
