package com.fpt.fptproducthunt.changelog.repository;

import com.fpt.fptproducthunt.common.entity.Changelog;
import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.metadata.ChangelogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface ChangelogRepository extends JpaRepository<Changelog, UUID> {
//    @Query(value = "Select * from Changelog ", nativeQuery = true)
    List<Changelog> findAllByProjectId(UUID projectId);

    @Query(value = "select * from changelog where projectid = :projectid and changelog_status in (:status)", nativeQuery = true)
    Page<Changelog> findAllByProjectId(@Param("projectid") String projectId, PageRequest pageRequest, @Param("status") int[] status);

    void deleteChangelogById(UUID id);
}
