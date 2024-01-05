package com.fpt.fptproducthunt.project.service;

import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.entity.User;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import com.fpt.fptproducthunt.project.dto.ProjectDTO;
import com.fpt.fptproducthunt.project.dto.ProjectDTOWithTopic;
import com.fpt.fptproducthunt.project.dto.ProjectListDTO;
import com.fpt.fptproducthunt.project.dto.ProjectListDTOWithTopic;
import com.fpt.fptproducthunt.project.exception.ProjectNotFoundException;
import com.fpt.fptproducthunt.project.repository.ProjectRepository;
import com.fpt.fptproducthunt.topic.dto.TopicDTO;
import com.fpt.fptproducthunt.user.repository.UserRepository;
import com.fpt.fptproducthunt.utility.BindUpUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectRepository projectRepository;

    @PersistenceContext
    EntityManager entityManager;
//    @Autowired
//    private UserRepository userRepository;
    private int noOfPages;

    @Override
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Override
    public Page<Project> getAll(int pageNo, int pageSize, String sortBy) {
        PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Project> pagedResult = projectRepository.findAll(paging);

        return pagedResult;
    }

    @Override
    public ProjectListDTOWithTopic getAll(int pageNo, int pageSize, String sortBy, String ascending, int statusType, String keyword, int[] milestoneType) {
        keyword = keyword.trim();
        List<Project> projectList = getAllList(pageNo, pageSize, sortBy, ascending, statusType, keyword, milestoneType);

//        if (keyword.length() == 0) {
//            projectList = getAllList(pageNo, pageSize, sortBy, ascending, statusType, milestoneType);
//        } else {
//            projectList = getAllList(pageNo, pageSize, sortBy, ascending, statusType, keyword, milestoneType);
//        }

//        List<ProjectDTO> projectDTOList = projectList.stream().map(
//                project -> new ProjectDTO(project.getId(),
//                        project.getName(),
//                        project.getLogo(),
//                        project.getSummary(),
//                        project.getDescription(),
//                        project.getSource(),
//                        project.getVoteQuantity(),
//                        project.getMilestone(),
//                        project.getCreatedDate(),
//                        project.getStatus())).collect(Collectors.toList());
//
//        ProjectListDTO projectListDTO = new ProjectListDTO();
//        projectListDTO.setProjectDTOList(projectDTOList);
//        projectListDTO.setPageSize(projectDTOList.size());
//        projectListDTO.setNumOfPages(noOfPages);

//        return projectListDTO;

        List<ProjectDTOWithTopic> projectDTOWithTopicList = projectList.stream().map(
        project -> new ProjectDTOWithTopic(project.getId(),
                project.getName(),
                project.getLogo(),
                project.getSummary(),
                project.getDescription(),
                project.getSource(),
                project.getVoteQuantity(),
                project.getMilestone(),
                project.getCreatedDate(),
                project.getStatus(),
                BindUpUtility.mapList(project.getTopics(), TopicDTO.class))).collect(Collectors.toList());

        ProjectListDTOWithTopic projectListDTOWithTopic = new ProjectListDTOWithTopic();
        projectListDTOWithTopic.setProjectDTOWithTopicList(projectDTOWithTopicList);
        projectListDTOWithTopic.setPageSize(projectDTOWithTopicList.size());
        projectListDTOWithTopic.setNumOfPages(noOfPages);

        return projectListDTOWithTopic;
//        return null;
    }
    public List<Project> getAllList(int pageNo, int pageSize, String sortBy, String ascending, int statusType, String keyword, int[] milestoneType) {
        PageRequest paging = ascending.equalsIgnoreCase("ASC") ?
                PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Page<Project> pagedResult = statusType == -1 ? projectRepository.findUsingFilter(keyword, paging, milestoneType):
                projectRepository.findUsingFilter(keyword, statusType, paging, milestoneType);

        noOfPages = pagedResult.getTotalPages();

        List<Project> projects = pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<Project>();

        return projects;
    }

    //cache here
    public List<Project> getAllList(int pageNo, int pageSize, String sortBy, String ascending, int statusType) {
        PageRequest paging = ascending.equalsIgnoreCase("ASC") ?
                PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Page<Project> pagedResult = statusType == -1 ? projectRepository.findAll(paging):
                                                        projectRepository.findAll(paging, statusType);

        noOfPages = pagedResult.getTotalPages();

        List<Project> projects = pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<Project>();

        return projects;
    }

    @Override
    public ProjectListDTO getAllByUserId(UUID id) {
        List<Project> projectList = projectRepository.findAllByFounderId(id);
        ProjectListDTO projectListDTO = convertProjectListToProjectListDTO(projectList);

        return projectListDTO;
    }

//    @Override
//    public ProjectListDTO getAllByProjectName(String name) {
//        List<Project> projectList = projectRepository.findByNameContainingIgnoreCase(name);
//        ProjectListDTO projectListDTO = convertProjectListToProjectListDTO(projectList);
//        return projectListDTO;
//    }

    private ProjectListDTO convertProjectListToProjectListDTO(List<Project> projectList) {
        ProjectListDTO projectListDTO = new ProjectListDTO();
        projectListDTO.setProjectDTOList(
                projectList.stream().map(
                        project -> new ProjectDTO(project.getId(),
                                project.getName(),
                                project.getLogo(),
                                project.getSummary(),
                                project.getDescription(),
                                project.getSource(),
                                project.getVoteQuantity(),
                                project.getMilestone(),
                                project.getCreatedDate(),
                                project.getStatus())).collect(Collectors.toList()));
        projectListDTO.setPageSize(projectList.size());
        projectListDTO.setNumOfPages(1);

        return projectListDTO;
    }

    @Override
    public Project get(UUID id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.isPresent() ? project.get() : null;
    }

    @Override
    public Project create(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project update(UUID id, String name, String summary, String description, String source, int milestone) {
        Optional<Project> fetchResult = projectRepository.findById(id);
        if(fetchResult.isPresent()) {
            Project project = fetchResult.get();

            project.setName(name);
            project.setSummary(summary);
            project.setDescription(description);
            project.setSource(source);
            project.setMilestone(milestone);

            return projectRepository.save(project);
        }
        else {
            throw new ProjectNotFoundException("Cannot find project with " + id);
        }
    }

    @Override
    public Project delete(UUID id) {
        Optional<Project> fetchResult = projectRepository.findById(id);
        if(fetchResult.isPresent()) {
            Project project = fetchResult.get();
            project.setStatus(ProjectStatus.DELETED);
            return projectRepository.save(project);
        }
        else {
            throw new ProjectNotFoundException("Cannot find project with " + id);
        }
    }

    @Override
    public Project changeStatus(UUID id, ProjectStatus status) {
        Optional<Project> fetchResult = projectRepository.findById(id);
        if(fetchResult.isPresent()) {
            Project project = fetchResult.get();
            project.setStatus(status);
            return projectRepository.save(project);
        }
        else {
            throw new ProjectNotFoundException("Cannot find project with " + id);
        }
    }

    @Override
    public Project changeLogo(UUID id, String logo) {
        Optional<Project> fetchResult = projectRepository.findById(id);
        if(fetchResult.isPresent()) {
            Project project = fetchResult.get();
            project.setLogo(logo);
            return projectRepository.save(project);
        }
        else {
            throw new ProjectNotFoundException("Cannot find project with " + id);
        }
    }

    @Override
    @Transactional
    public void addTopic(UUID projectId, UUID topicId) {
        entityManager.createNativeQuery("insert into project_topic (topicid, projectid) values (?, ?)")
                .setParameter(1, topicId.toString())
                .setParameter(2, projectId.toString())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deleteTopic(UUID projectId, UUID topicId) {
        entityManager.createNativeQuery("delete from project_topic where topicid = ? and projectid = ?")
                .setParameter(1, topicId.toString())
                .setParameter(2, projectId.toString())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void addMentor(UUID projectId, UUID mentorId) {
        entityManager.createNativeQuery("insert into projectmentor (mentorid, projectid) values (?, ?)")
                .setParameter(1, mentorId.toString())
                .setParameter(2, projectId.toString())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deleteMentor(UUID projectId, UUID mentorId) {
        entityManager.createNativeQuery("delete from projectmentor where mentorid = ? and projectid = ?")
                .setParameter(1, mentorId.toString())
                .setParameter(2, projectId.toString())
                .executeUpdate();
    }

    public Project updateVoteQuantity(UUID projectId, int value) {
        Optional<Project> fetchResult = projectRepository.findById(projectId);
        if(fetchResult.isPresent()) {
            Project project = fetchResult.get();
            project.setVoteQuantity(project.getVoteQuantity() + value);

//        entityManager.createNativeQuery("update project set vote_quantity = vote_quantity + ? where id = ?")
//                .setParameter(1, value)
//                .setParameter(2, projectId.toString())
//                .executeUpdate();
            return projectRepository.save(project);
        }
        else {
            throw new ProjectNotFoundException("Cannot find project with " + projectId);
        }
    }

    @Override
    public Object findVote(UUID projectId, UUID userId) {
        return projectRepository.findVote(projectId.toString(), userId.toString());
    }

    @Override
    @Transactional
    public void addVote(UUID projectId, UUID userId) {
        entityManager.createNativeQuery("insert into vote (userid, projectid) values (?, ?)")
                .setParameter(1, userId.toString())
                .setParameter(2, projectId.toString())
                .executeUpdate();
        updateVoteQuantity(projectId, 1);
    }

    @Override
    @Transactional
    public void deleteVote(UUID projectId, UUID userId) {
        entityManager.createNativeQuery("delete from vote where userid = ? and projectid = ?")
                .setParameter(1, userId.toString())
                .setParameter(2, projectId.toString())
                .executeUpdate();
        updateVoteQuantity(projectId, -1);
    }
}
