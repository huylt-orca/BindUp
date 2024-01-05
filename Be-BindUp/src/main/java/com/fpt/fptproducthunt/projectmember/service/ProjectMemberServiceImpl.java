package com.fpt.fptproducthunt.projectmember.service;

import com.fpt.fptproducthunt.common.entity.Changelog;
import com.fpt.fptproducthunt.common.entity.ProjectMember;
import com.fpt.fptproducthunt.projectmember.repository.ProjectMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Override
    public List<ProjectMember> getAll(UUID projectId) {
        return projectMemberRepository.findAllByProjectId(projectId);
    }

    @Override
    public Page<ProjectMember> getAll(UUID projectId, int pageNo, int pageSize, String sortBy) {
        PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<ProjectMember> pagedResult = projectMemberRepository.findAllByProjectId(projectId, paging);

        return pagedResult;
    }

    @Override
    public Optional<ProjectMember> get(UUID id) {
        return projectMemberRepository.findById(id);
    }

    @Override
    public ProjectMember create(ProjectMember projectMember) {
        return projectMemberRepository.save(projectMember);
    }

    @Override
    public ProjectMember update(UUID id, String role, String title, String name) {
        ProjectMember projectMember = null;

        try {
             projectMember = projectMemberRepository.findById(id).orElseThrow(() -> new Exception());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        projectMember.setTitle(title);
        projectMember.setRole(role);
        projectMember.setName(name);

        return projectMemberRepository.save(projectMember);
    }

    @Override
    public void delete(UUID id) {
        projectMemberRepository.deleteById(id);
    }
}
