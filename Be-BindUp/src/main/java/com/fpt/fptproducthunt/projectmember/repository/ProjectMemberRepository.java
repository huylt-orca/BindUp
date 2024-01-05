package com.fpt.fptproducthunt.projectmember.repository;

import com.fpt.fptproducthunt.common.entity.Changelog;
import com.fpt.fptproducthunt.common.entity.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {
//    @Query(value = "Select * from Changelog ", nativeQuery = true)
    List<ProjectMember> findAllByProjectId(UUID projectId);

//    @Query(value = "Select * from Project where status != 2", nativeQuery = true)
    Page<ProjectMember> findAllByProjectId(UUID projectId, PageRequest pageRequest);

    void deleteById(UUID id);
}
