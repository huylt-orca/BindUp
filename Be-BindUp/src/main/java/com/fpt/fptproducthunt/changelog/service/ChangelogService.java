package com.fpt.fptproducthunt.changelog.service;

import com.fpt.fptproducthunt.common.entity.Changelog;
import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.metadata.ChangelogStatus;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChangelogService {
    List<Changelog> getAll(UUID projectId);
    Page<Changelog> getAll(UUID projectId, int pageNo, int pageSize, String sortBy, String ascending, ChangelogStatus status);
    Optional<Changelog> get(UUID id);
    Changelog create(Changelog changelog);
    Changelog update(UUID id, String description, String title);
    void delete(UUID id);

    Changelog changeStatus(UUID id, ChangelogStatus status);
//    Changelog changeStatus(UUID id, ProjectStatus newStatus);
}
