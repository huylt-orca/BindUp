package com.fpt.fptproducthunt.projectimage.service;

import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.entity.ProjectImage;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import com.fpt.fptproducthunt.projectimage.repository.ProjectImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectImageServiceImpl implements ProjectImageService {
    @Autowired
    private ProjectImageRepository projectImageRepository;
    @Override
    public List<ProjectImage> getAll(UUID projectId) {
        return projectImageRepository.findAllByProjectId(projectId);
    }
    @Override
    public ProjectImage create(ProjectImage projectImage) {
        return projectImageRepository.save(projectImage);
    }
    @Override
    public void delete(UUID id) {
        projectImageRepository.delete(projectImageRepository.findById(id).get());
    }
}
