package com.fpt.fptproducthunt.application.repository;

import com.fpt.fptproducthunt.common.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    Optional<Application> findById(UUID id);
    @Query(value = "select * from application where status = :status and projectid = :projectid", nativeQuery = true)
    Page<Application> findAll(@Param("projectid") String projectId, @Param("status") int status, PageRequest paging);

    @Query(value = "select * from application where projectid = :projectid", nativeQuery = true)
    Page<Application> findAll(@Param("projectid") String projectId, PageRequest paging);

    @Query(value = "select * from application where userid = :userid and status = :status", nativeQuery = true)
    Page<Application> findAllByUserId(@Param("userid") String userId, @Param("status") int status, PageRequest paging);

    @Query(value = "select * from application where userid = :userid", nativeQuery = true)
    Page<Application> findAllByUserId(@Param("userid") String userId, PageRequest paging);

    @Query(value = "select * from application where projectid = :projectid and userid = :userid and status = 0", nativeQuery = true)
    Application find(@Param("projectid") String projectId, @Param("userid") String userId);

    @Query(value = "select * from application where jobid = :jobId and status = :status", nativeQuery = true)
    Page<Application> findAllByJobId(@Param("jobId") String jobId, @Param("status") int status, PageRequest paging);

    @Query(value = "select * from application where jobid = :jobId", nativeQuery = true)
    Page<Application> findAllByJobId(@Param("jobId") String jobId, PageRequest paging);
}
