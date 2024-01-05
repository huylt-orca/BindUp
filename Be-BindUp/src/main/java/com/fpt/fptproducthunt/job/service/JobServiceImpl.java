package com.fpt.fptproducthunt.job.service;

import com.fpt.fptproducthunt.common.entity.Application;
import com.fpt.fptproducthunt.common.entity.Job;
import com.fpt.fptproducthunt.common.entity.ProjectMember;
import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import com.fpt.fptproducthunt.common.metadata.JobStatus;
import com.fpt.fptproducthunt.job.repository.JobRepository;
import com.fpt.fptproducthunt.projectmember.repository.ProjectMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobRepository jobRepository;

    @Override
    public Page<Job> getAll(int pageNo, int pageSize, String sortBy, String ascending, String keyword) {
        PageRequest pageable = PageRequest.of(pageNo, pageSize, ascending.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        return jobRepository.findAll(keyword, pageable);
    }
    @Override
    public List<Job> getAll(UUID projectId) {
        return jobRepository.findAllByProjectId(projectId);
    }

    @Override
    public Page<Job> getAll(UUID projectId, int pageNo, int pageSize, String sortBy) {
        PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Job> pagedResult = jobRepository.findAllByProjectId(projectId, paging);

        return pagedResult;
    }

    @Override
    public Job get(UUID id) {
        return jobRepository.findById(id).get();
    }

    @Override
    public Job create(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Job update(UUID id, String name, String description, Date dueDate) {
        Job job = null;

        try {
            job = jobRepository.findById(id).orElseThrow(() -> new Exception());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        job.setName(name);
        job.setDescription(description);
        job.setDueDate(dueDate);

        return jobRepository.save(job);
    }

    @Override
    public void delete(UUID id) {
        jobRepository.deleteById(id);
    }

    @Override
    public Job changeStatus(UUID id, JobStatus status) {
        Job job = get(id);
        job.setJobStatus(status);
        return jobRepository.save(job);
    }
}
