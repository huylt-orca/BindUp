package com.fpt.fptproducthunt.projectmember.service;

import com.fpt.fptproducthunt.common.entity.Changelog;
import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.entity.ProjectMember;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectMemberService {
    List<ProjectMember> getAll(UUID projectId);
    Page<ProjectMember> getAll(UUID projectId, int pageNo, int pageSize, String sortBy);
    Optional<ProjectMember> get(UUID id);
    ProjectMember create(ProjectMember projectMember);
    ProjectMember update(UUID id, String role, String title, String name);
    void delete(UUID id);
//    Changelog changeStatus(UUID id, ProjectStatus newStatus);
}
