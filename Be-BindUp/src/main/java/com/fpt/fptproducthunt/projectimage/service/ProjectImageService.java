package com.fpt.fptproducthunt.projectimage.service;

import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.entity.ProjectImage;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectImageService {
    List<ProjectImage> getAll(UUID projectId);
    ProjectImage create(ProjectImage projectImage);
    void delete(UUID id);

}
