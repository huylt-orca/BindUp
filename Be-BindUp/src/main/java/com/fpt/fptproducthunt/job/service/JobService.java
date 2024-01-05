package com.fpt.fptproducthunt.job.service;

import com.fpt.fptproducthunt.common.entity.Application;
import com.fpt.fptproducthunt.common.entity.Job;
import com.fpt.fptproducthunt.common.entity.ProjectMember;
import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import com.fpt.fptproducthunt.common.metadata.JobStatus;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobService {
    List<Job> getAll(UUID projectId);
    Page<Job> getAll(int pageNo, int pageSize, String sortBy, String ascending, String keyword);
    Page<Job> getAll(UUID projectId, int pageNo, int pageSize, String sortBy);
    Job get(UUID id);
    Job create(Job job);
    Job update(UUID id, String name, String description, Date dueDate);
    void delete(UUID id);
    Job changeStatus(UUID id, JobStatus jobStatus);
}
