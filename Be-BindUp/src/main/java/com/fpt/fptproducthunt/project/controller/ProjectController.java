package com.fpt.fptproducthunt.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.fptproducthunt.application.dto.ApplicationDTO;
import com.fpt.fptproducthunt.changelog.dto.ChangelogDTO;
import com.fpt.fptproducthunt.cloudmessaging.service.FirebaseMessagingService;
import com.fpt.fptproducthunt.common.dto.NotificationMessage;
import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.*;
import com.fpt.fptproducthunt.job.dto.JobDTO;
import com.fpt.fptproducthunt.mentor.dto.MentorDTO;
import com.fpt.fptproducthunt.notification.service.NotificationService;
import com.fpt.fptproducthunt.project.dto.*;
import com.fpt.fptproducthunt.projectimage.dto.ProjectImageDTO;
import com.fpt.fptproducthunt.projectimage.service.ProjectImageService;
import com.fpt.fptproducthunt.projectmember.dto.ProjectMemberDTO;
import com.fpt.fptproducthunt.topic.dto.TopicDTO;
import com.fpt.fptproducthunt.user.dto.UserDTO;
import com.fpt.fptproducthunt.utility.BindUpUtility;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import com.fpt.fptproducthunt.firebasestore.FirebaseService;
import com.fpt.fptproducthunt.project.repository.ProjectRepository;
import com.fpt.fptproducthunt.project.service.ProjectService;
import com.fpt.fptproducthunt.user.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.http.entity.ContentType;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/projects")
@CrossOrigin
@RestController
@Slf4j
@Tag(name = "Project APIs")
public class ProjectController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private ProjectImageService projectImageService;

    @Operation(summary = "Fetch project list. Sort by (id, votes, name). StatusType = {-1, 0, 1, 2 ,3} <==> {All, Public, Private, Rejected, Deleted}." +
            "ASC = Ascending / DESC = Descending by sort by param, milestone = {0, 1, 2, 3} <==> {idea, upcoming, launching, finished")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query project list successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@RequestParam(defaultValue = "0") int pageNo,
                                @RequestParam(defaultValue = "10") int pageSize,
                                @RequestParam(defaultValue = "created_date") String sortBy,
                                @RequestParam(defaultValue = "0") int statusType,
                                @RequestParam(defaultValue = "ASC") String ascending,
                                @RequestParam(defaultValue = "") String nameKeyWord,
                                @RequestParam(defaultValue = "") int[] milestoneType) {
        if (milestoneType == null || milestoneType.length == 0) {
            milestoneType = new int[]{0, 1, 2, 3};
        } else {
            for (int i = 0;i < milestoneType.length;++i) {
                if (0 > milestoneType[i] || milestoneType[i] > 3) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(
                            "ERROR", "Milestone value should be in range (0, 3)", ""));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("OK", "Project list found",
                    projectService.getAll(pageNo, pageSize, sortBy, ascending, statusType, nameKeyWord, milestoneType))
        );
    }

    ProjectDetailDTO convertProjectToProjectDetailDTO(Project project) {
        this.modelMapper = new ModelMapper();

        ProjectDTO projectDTO = this.modelMapper.map(project, ProjectDTO.class);
        ProjectDetailDTO projectDetailDTO = this.modelMapper.map(projectDTO, ProjectDetailDTO.class);

        projectDetailDTO.setMentors(BindUpUtility.mapList(project.getMentors(), MentorDTO.class));
        projectDetailDTO.setImages(BindUpUtility.mapList(project.getImages(), ProjectImageDTO.class));
        projectDetailDTO.setMembers(BindUpUtility.mapList(project.getMembers(), ProjectMemberDTO.class));
        projectDetailDTO.setJobs(BindUpUtility.mapList(project.getJobs(), JobDTO.class));
        projectDetailDTO.setApplications(BindUpUtility.mapList(project.getApplications(), ApplicationDTO.class));
        projectDetailDTO.setChangelogs(BindUpUtility.mapList(project.getChangelogs(), ChangelogDTO.class));
        projectDetailDTO.setFounder(this.modelMapper.map(project.getFounder(), UserDTO.class));
        projectDetailDTO.setTopics(BindUpUtility.mapList(project.getTopics(), TopicDTO.class));

        return projectDetailDTO;
    }

    @Operation(summary = "Fetch project by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query project successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getProjectById(@PathVariable UUID id) {
        Project foundProject = projectService.get(id);

        ProjectDetailDTO projectDetailDTO = convertProjectToProjectDetailDTO(foundProject);

        return foundProject != null ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Project found", projectDetailDTO)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Cannot find project with id = " + id, "")
                );
    }

    private Project fromCreatedProjectDTOToProject(CreatedProjectDTO createdProjectDTO) {
        this.modelMapper = new ModelMapper();

        TypeMap<CreatedProjectDTO, Project> propertyMapper = this.modelMapper.createTypeMap(CreatedProjectDTO.class, Project.class);
        propertyMapper.addMappings(mapper -> mapper.skip(Project::setFounder));

        Project project = modelMapper.map(createdProjectDTO, Project.class);

//        User user = userService.findByUsername(createdProjectDTO.getFounderUsername());

        project.setFounder(userService.findByUserID(createdProjectDTO.getFounderId()).get());
        project.setCreatedDate(BindUpUtility.getCurrentDate());
        project.setStatus(ProjectStatus.PRIVATE);

        return project;
    }

    @Operation(summary = "Create project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create project successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<ResponseObject> createProject(@RequestBody CreatedProjectDTO createdProjectDTO) {
        Project createdProject = fromCreatedProjectDTOToProject(createdProjectDTO);

        Project addedProject = projectService.create(createdProject);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Project created", addedProject.getId())
        );
    }
    @Operation(summary = "Update project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update project successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PutMapping("")
    public ResponseEntity<ResponseObject> updateProject(@RequestParam UUID id, @RequestBody UpdateProjectDTO updateProjectDTO) {
        Project foundProject = projectService.update(
                id,
                updateProjectDTO.getName(),
                updateProjectDTO.getSummary(),
                updateProjectDTO.getDescription(),
                updateProjectDTO.getSource(),
                updateProjectDTO.getMilestone());
        return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Project updated", "")
                );
    }

    @Operation(summary = "Delete project by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete project successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteProject(@PathVariable UUID id) {
        Project foundProject = projectService.get(id);

        if(foundProject != null) {
            projectService.delete(foundProject.getId());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Project deleted", "")
            );
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", "Cannot find project with id = " + id, "")
            );
        }
    }
    @Operation(summary = "Upload image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload image successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping(value="/{projectId}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> uploadImage(@PathVariable UUID projectId, @RequestPart(required = true) MultipartFile[] imageFile) throws FirebaseAuthException {
        try {

//            log.info("Calling firebase service");
            for(int i = 0;i < imageFile.length;++i) {
                String fileName = firebaseService.uploadImage(imageFile[i]);
                ProjectImage projectImage = new ProjectImage();
                projectImage.setProject(projectService.get(projectId));
                projectImage.setDirectory(fileName);
                projectImageService.create(projectImage);
            }
//            log.info("Returning response with image URL");

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Images added", "")
            );
        } catch (IOException e) {
//            log.info(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Upload logo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload image successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping(value="/{projectId}/logo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> uploadLogo(@PathVariable UUID projectId, @RequestPart(required = true) MultipartFile imageFile) throws FirebaseAuthException {
        try {
//            log.info("Calling firebase service");
            String fileName = firebaseService.uploadImage(imageFile);
            projectService.changeLogo(projectId, fileName);
//            log.info("Returning response with image URL");

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Link image = " + fileName, fileName)
            );
        } catch (IOException e) {
//            log.info(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//    @Operation(summary = "Get images of project")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Upload image successfully",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ResponseObject.class)) }),
//            @ApiResponse(responseCode = "403", description = "Don't have permission",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ResponseObject.class)) })})
//    @GetMapping(value="/{projectId}/image/")
//    public ResponseEntity<ResponseObject> getImages(@PathVariable UUID projectId) {
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "List images found", projectImageService.getAll(projectId)));
//    }
    @Operation(summary = "Delete image of project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete image successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @DeleteMapping(value = "/{projectId}/image/{id}")
    public ResponseEntity<ResponseObject> deleteImage(@PathVariable UUID projectId, @PathVariable UUID id) {
        projectImageService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Deleted image", ""));
    }
    @Operation(summary = "Change status of project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Change project status successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseObject> changeStatus(@PathVariable UUID id, @RequestParam ProjectStatus projectStatus) {
        Project project = projectService.changeStatus(id, projectStatus);

        String notificationTitle;
        String notificationBody;

        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setRecipientToken(project.getFounder().getAccount().getDeviceToken());

        Notification notification = new Notification();
        notification.setLogo(project.getLogo());
        notification.setCreatedTime(BindUpUtility.getCurrentTimestamp());
        notification.setRecipient(project.getFounder());

        if(projectStatus == ProjectStatus.PUBLIC) {
            notificationBody = "Admin changed your project " + project.getName() + " to public!";
            notificationMessage.setBody(notificationBody);
            notification.setBody(notificationBody);
        }

        if(projectStatus == ProjectStatus.PRIVATE) {
            notificationBody = "Admin changed your project " + project.getName() + " to private!";
            notificationMessage.setBody(notificationBody);
            notification.setBody(notificationBody);
        }

        if(projectStatus == ProjectStatus.DELETED) {
            notificationBody = "Admin deleted your project " + project.getName();
            notificationMessage.setBody(notificationBody);
            notification.setBody(notificationBody);
        }

        if(projectStatus == ProjectStatus.REJECTED) {
            notificationBody = "Admin rejected your project " + project.getName();
            notificationMessage.setBody(notificationBody);
            notification.setBody(notificationBody);
        }
        notificationTitle = "Your project has changed status";
        notificationMessage.setTitle(notificationTitle);
        notification.setTitle(notificationTitle);

        Map<String, String> tmp = new HashMap<>();

        notificationMessage.setData(tmp);
        notificationMessage.setImage("");

        firebaseMessagingService.sendNotificationByToken(notificationMessage);
        notificationService.saveNotification(notification);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Status changed", ""));
    }

    @Operation(summary = "Add topics to project. Input projectId and topicId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add topics successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("/{projectId}/topic")
    public ResponseEntity<ResponseObject> addTopic(@PathVariable UUID projectId, @RequestParam UUID[] topicIds) {
        for(UUID topicId: topicIds) {
//            System.out.println(topicId);
            projectService.addTopic(projectId, topicId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Saved topics", ""));
    }


    @Operation(summary = "Delete topic of project. Input projectId and topicId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete topic successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @DeleteMapping("/{projectId}/topic")
    public ResponseEntity<ResponseObject> deleteTopic(@PathVariable UUID projectId, @RequestParam UUID topicId) {
        projectService.deleteTopic(projectId, topicId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Deleted topics", ""));
    }

    @Operation(summary = "Add mentor to project. Input projectId and mentorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add mentor successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("/{projectId}/mentor")
    public ResponseEntity<ResponseObject> addMentor(@PathVariable UUID projectId, @RequestParam UUID mentorId) {
        projectService.addMentor(projectId, mentorId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Saved mentor", ""));
    }


    @Operation(summary = "Delete mentor of project. Input projectId and mentorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete mentor successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @DeleteMapping("/{projectId}/mentor")
    public ResponseEntity<ResponseObject> deleteMentor(@PathVariable UUID projectId, @RequestParam UUID mentorId) {
        projectService.deleteMentor(projectId, mentorId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Deleted mentor", ""));
    }

    @Operation(summary = "Vote project. Input projectId and userId. Auto de-vote if vote found and vice versa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("/{projectId}/vote")
    public ResponseEntity<ResponseObject> voteProject(@PathVariable UUID projectId, @RequestParam UUID userId) {

//        System.out.println(BindUpUtility.getCurrentTime());
//        projectService.addMentor(projectId, mentorId);
//        projectService.addVote(projectId, userId);
        if (projectService.findVote(projectId, userId) != null) {
            projectService.deleteVote(projectId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Devoted", projectService.get(projectId).getVoteQuantity()));
        } else {
            projectService.addVote(projectId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Upvoted", projectService.get(projectId).getVoteQuantity()));

        }
     }
}
