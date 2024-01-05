package com.fpt.fptproducthunt.application.service;

import com.fpt.fptproducthunt.common.entity.Application;
import com.fpt.fptproducthunt.common.entity.Job;
import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {

    Page<Application> getAll(UUID projectId, int pageNo, int pageSize, String sortBy, String ascending, int status);
    Application get(UUID id);
    Application create(Application application);
    Application update(UUID id, String description);
    void delete(UUID id);
    Application changeStatus(UUID id, ApplicationStatus status);
    Page<Application> getAllByUserId(UUID userId, int pageNo, int pageSize, String sortBy, String ascending, int status);
    boolean findByProjectIdAndUserId(UUID userId, UUID projectId);
    Page<Application> getAllByJobId(UUID jobId, int pageNo, int pageSize, String sortBy, String ascending, ApplicationStatus status, String keyword);
}
