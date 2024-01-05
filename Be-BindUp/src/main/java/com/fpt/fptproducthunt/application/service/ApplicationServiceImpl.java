package com.fpt.fptproducthunt.application.service;

import com.fpt.fptproducthunt.application.repository.ApplicationRepository;
import com.fpt.fptproducthunt.common.entity.Application;
import com.fpt.fptproducthunt.common.entity.Job;
import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Service
public class ApplicationServiceImpl implements ApplicationService{
    @Autowired
    private ApplicationRepository applicationRepository;
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public Page<Application> getAll(UUID projectId, int pageNo, int pageSize, String sortBy, String ascending, int status) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, ascending.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Application> applicationPage = status == -1 ? applicationRepository.findAll(projectId.toString(), pageRequest) :
                                            applicationRepository.findAll(projectId.toString(), status, pageRequest);
        return applicationPage;
    }
    @Override
    public Page<Application> getAllByUserId(UUID userId, int pageNo, int pageSize, String sortBy, String ascending, int status) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, ascending.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Application> applicationPage = status == -1 ? applicationRepository.findAllByUserId(userId.toString(), pageRequest) :
                applicationRepository.findAllByUserId(userId.toString(), status, pageRequest);
        return applicationPage;
    }

    @Override
    public Application get(UUID id) {
        return applicationRepository.findById(id).get();
    }

    @Override
    public Application create(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    public Application update(UUID id, String description) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public Application changeStatus(UUID id, ApplicationStatus status) {
        Application application = get(id);
        application.setStatus(status);
        return applicationRepository.save(application);
    }

    @Override
    public boolean findByProjectIdAndUserId(UUID userId, UUID projectId) {
        Application application = applicationRepository.find(projectId.toString(), userId.toString());
         return application == null ? true : false;
    }

    @Override
    public Page<Application> getAllByJobId(UUID jobId, int pageNo, int pageSize, String sortBy, String ascending, ApplicationStatus status, String keyword) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, ascending.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Application> applicationPage = status == null ? applicationRepository.findAllByJobId(jobId.toString(), pageRequest) :
                applicationRepository.findAllByJobId(jobId.toString(), status.ordinal(), pageRequest);
        return applicationPage;
    }
}
