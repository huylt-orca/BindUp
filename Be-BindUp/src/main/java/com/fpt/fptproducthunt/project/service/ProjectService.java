package com.fpt.fptproducthunt.project.service;

import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import com.fpt.fptproducthunt.project.dto.ProjectListDTO;
import com.fpt.fptproducthunt.project.dto.ProjectListDTOWithTopic;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {
    List<Project> getAll();
    Page<Project> getAll(int pageNo, int pageSize, String sortBy);
    ProjectListDTOWithTopic getAll(int pageNo, int pageSize, String sortBy, String ascending, int statusType, String keyword, int[] milestoneType);
    ProjectListDTO getAllByUserId(UUID id);
    Project get(UUID id);
    Project create(Project project);
    Project update(UUID id, String name, String summary, String description, String source, int milestone);
    Project delete(UUID id);
    Project changeStatus(UUID id, ProjectStatus newStatus);
    Project changeLogo(UUID id, String logo);

    void addTopic(UUID projectId, UUID topicId);
    void deleteTopic(UUID projectId, UUID topicId);
    void addMentor(UUID projectId, UUID mentorId);
    void deleteMentor(UUID projectId, UUID mentorId);

    Object findVote(UUID projectId, UUID userId);
    void addVote(UUID projectId, UUID userId);
    void deleteVote(UUID projectId, UUID userId);
}
