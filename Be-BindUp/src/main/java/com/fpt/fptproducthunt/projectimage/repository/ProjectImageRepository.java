package com.fpt.fptproducthunt.projectimage.repository;

import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.entity.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, UUID> {
    List<ProjectImage> findAllByProjectId(UUID projectId);
    void deleteProjectImageById(UUID id);
    ProjectImage findProjectImageById(UUID id);

    void delete(ProjectImage projectImage);
}
