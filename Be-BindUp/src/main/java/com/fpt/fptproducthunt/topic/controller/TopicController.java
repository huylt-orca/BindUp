package com.fpt.fptproducthunt.topic.controller;

import com.fpt.fptproducthunt.common.dto.ErrorMessage;
import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.entity.Topic;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import com.fpt.fptproducthunt.project.dto.ProjectDTO;
import com.fpt.fptproducthunt.project.dto.ProjectListDTO;
import com.fpt.fptproducthunt.topic.dto.CreatedTopicDTO;
import com.fpt.fptproducthunt.topic.dto.TopicDTO;
import com.fpt.fptproducthunt.topic.dto.TopicDTOList;
import com.fpt.fptproducthunt.topic.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/topics")
@Tag(name = "Topic APIs")
public class TopicController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TopicService topicService;

    @Operation(summary = "Fetch topic list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query topic list successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@RequestParam(defaultValue = "0") int pageNo,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @RequestParam(defaultValue = "id") String sortBy) {
        List<Topic> topicList = topicService.getAll();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Topic> topicPaging = new PageImpl<>(topicList, pageable, topicList.size());

        List<TopicDTO> topics = topicPaging.hasContent() ? topicPaging.getContent().stream().map(
                topic -> new TopicDTO(
                        topic.getId(),
                        topic.getName(),
                        topic.getDescription(),
                        topic.getShortName())).collect(Collectors.toList()) : new ArrayList<>();
        TopicDTOList topicDTOList = new TopicDTOList();
        topicDTOList.setTopicDTOList(topics);
        topicDTOList.setPageSize(topics.size());
        topicDTOList.setNoOfPages(topicPaging.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Topic list found", topicDTOList));
    }

    @Operation(summary = "Fetch topic by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query topic successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "404", description = "Cannot find topic with ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)) })})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getTopicById(@PathVariable UUID id) {
        Topic topicFetchResult = topicService.get(id);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Topic found", topicFetchResult));
    }

    private Topic fromCreatedTopicDTOToTopic(CreatedTopicDTO createdTopicDTO) {
        this.modelMapper = new ModelMapper();
        return modelMapper.map(createdTopicDTO, Topic.class);
    }

    @Operation(summary = "Create topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create topic successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("")
    public ResponseEntity<ResponseObject> addTopic(@RequestBody CreatedTopicDTO createdTopicDTO) {
        Topic createdTopic = fromCreatedTopicDTOToTopic(createdTopicDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(new ResponseObject("OK", "Topic created", topicService.createTopic(createdTopic)));
    }

    @Operation(summary = "Update topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update topic successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "404", description = "Cannot find topic with ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)) })})
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateTopic(@PathVariable UUID id, @RequestBody Topic topic) {
        Topic updatedTopic = topicService.updateTopic(id, topic);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Topic updated", updatedTopic));
    }

    @Operation(summary = "Delete topic by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete topic successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "404", description = "Cannot find topic with ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)) })})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteTopic(@PathVariable UUID id) {
        topicService.deleteTopic(id);
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(new ResponseObject("OK", "Topic deleted", ""));
    }

    @Operation(summary = "Get project by topic id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get projects by topic id successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/{id}/projects")
    public ResponseEntity<ResponseObject> getProjectsByTopic(@PathVariable UUID id,
                                                             @RequestParam(defaultValue = "0") int pageNo,
                                                             @RequestParam(defaultValue = "10") int pageSize,
                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                             @RequestParam(defaultValue = "ASC") String ascending,
                                                             @RequestParam(defaultValue = "") String nameKeyword) {
        Topic topic = topicService.get(id);

        List<Project> projectList = new ArrayList<>();
        List<Project> tmpProjects = topic.getProjects();
        for(int i = 0;i < tmpProjects.size();++i) {
            if (tmpProjects.get(i).getName().toLowerCase().contains(nameKeyword.toLowerCase())) {
                if (tmpProjects.get(i).getStatus() == ProjectStatus.PUBLIC)
                    projectList.add(tmpProjects.get(i));
            }
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, ascending.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Project> projectPage = new PageImpl<>(projectList, pageable, projectList.size());

        List<ProjectDTO> projectDTOList = projectPage.hasContent() ? projectList.stream().map(
                project -> new ProjectDTO(project.getId(),
                        project.getName(),
                        project.getLogo(),
                        project.getSummary(),
                        project.getDescription(),
                        project.getSource(),
                        project.getVoteQuantity(),
                        project.getMilestone(),
                        project.getCreatedDate(),
                        project.getStatus())).collect(Collectors.toList()) : new ArrayList<>();

        ProjectListDTO projectListDTO = new ProjectListDTO();
        projectListDTO.setProjectDTOList(projectDTOList);
        projectListDTO.setNumOfPages(pageable.getPageNumber());
        projectListDTO.setPageSize(projectDTOList.size());

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Found projects of topic " + topic.getName(), projectDTOList));
    }
}
