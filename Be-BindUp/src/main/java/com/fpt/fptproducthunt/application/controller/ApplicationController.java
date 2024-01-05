package com.fpt.fptproducthunt.application.controller;

import com.fpt.fptproducthunt.application.dto.ApplicationDTO;
import com.fpt.fptproducthunt.application.dto.ApplicationDTOList;
import com.fpt.fptproducthunt.application.dto.ApplicationDetailDTO;
import com.fpt.fptproducthunt.application.dto.CreatedApplicationDTO;
import com.fpt.fptproducthunt.application.service.ApplicationService;
import com.fpt.fptproducthunt.cloudmessaging.service.FirebaseMessagingService;
import com.fpt.fptproducthunt.common.dto.NotificationMessage;
import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.Application;
import com.fpt.fptproducthunt.common.entity.Job;
import com.fpt.fptproducthunt.common.entity.Notification;
import com.fpt.fptproducthunt.common.entity.ProjectMember;
import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import com.fpt.fptproducthunt.job.dto.CreatedJobDTO;
import com.fpt.fptproducthunt.job.dto.JobDTO;
import com.fpt.fptproducthunt.job.dto.JobDTOList;
import com.fpt.fptproducthunt.job.service.JobService;
import com.fpt.fptproducthunt.notification.service.NotificationService;
import com.fpt.fptproducthunt.project.dto.ProjectDTO;
import com.fpt.fptproducthunt.project.service.ProjectService;
import com.fpt.fptproducthunt.projectmember.service.ProjectMemberService;
import com.fpt.fptproducthunt.user.dto.UserDTO;
import com.fpt.fptproducthunt.user.service.UserService;
import com.fpt.fptproducthunt.utility.BindUpUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/applications")
@CrossOrigin
@RestController
@Tag(name = "Application APIs")
public class ApplicationController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ProjectMemberService projectMemberService;
    @Autowired
    private JobService jobService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private NotificationService notificationService;
    @Operation(summary = "Fetch application list by projectId. sort by id/created_date, " +
            "ASC/DESC = Ascending/Descending, status = {-1, 0, 1, 2} => {ALL, PENDING, ACCEPTED, REJECTED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query project job successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@RequestParam(defaultValue = "", required = true) UUID projectId,
                                                 @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                 @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                 @RequestParam(defaultValue = "created_timestamp", required = false) String sortBy,
                                                 @RequestParam(defaultValue = "DESC", required = true) String ascending,
                                                 @RequestParam(defaultValue = "0", required = true) int status) {
        Page<Application> applicationPage = applicationService.getAll(projectId, pageNo, pageSize, sortBy, ascending, status);

        List<Application> applicationList = applicationPage.hasContent() ? applicationPage.getContent() : new ArrayList<>();
        List<ApplicationDetailDTO> applicationDetailDTOList = new ArrayList<>();
        for(Application application: applicationList) {
            applicationDetailDTOList.add(fromApplicationToApplicationDetailDTO(application));
        }

        ApplicationDTOList applicationDTOList = new ApplicationDTOList();
        applicationDTOList.setApplicationDTOList(applicationDetailDTOList);
        applicationDTOList.setPageSize(applicationList.size());
        applicationDTOList.setNumOfPages(applicationPage.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Application list found", applicationDTOList));
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseObject> getAllByUserId(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "created_timestamp", required = false) String sortBy,
            @RequestParam(defaultValue = "DESC", required = true) String ascending,
            @RequestParam(defaultValue = "0", required = true) int status) {
        Page<Application> applicationPage = applicationService.getAllByUserId(userId, pageNo, pageSize, sortBy, ascending, status);

        List<Application> applicationList = applicationPage.hasContent() ? applicationPage.getContent() : new ArrayList<>();
        List<ApplicationDetailDTO> applicationDetailDTOList = new ArrayList<>();
        for(Application application: applicationList) {
            applicationDetailDTOList.add(fromApplicationToApplicationDetailDTO(application));
        }

        ApplicationDTOList applicationDTOList = new ApplicationDTOList();
        applicationDTOList.setApplicationDTOList(applicationDetailDTOList);
        applicationDTOList.setPageSize(applicationList.size());
        applicationDTOList.setNumOfPages(applicationPage.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Application list found", applicationDTOList));
    }

    private ApplicationDetailDTO fromApplicationToApplicationDetailDTO(Application application) {
        this.modelMapper = new ModelMapper();
        ApplicationDTO applicationDTO = this.modelMapper.map(application, ApplicationDTO.class);
        ApplicationDetailDTO applicationDetailDTO = this.modelMapper.map(applicationDTO, ApplicationDetailDTO.class);

        applicationDetailDTO.setJobDTO(this.modelMapper.map(application.getJob(), JobDTO.class));
        applicationDetailDTO.setProjectDTO(this.modelMapper.map(application.getProject(), ProjectDTO.class));
        applicationDetailDTO.setUserDTO(this.modelMapper.map(application.getApplicant(), UserDTO.class));

        return applicationDetailDTO;
    }

    private Application fromCreatedApplicationDTOToApplication(CreatedApplicationDTO createdApplicationDTO) {
        Application application = new Application();
        application.setCreatedDate(BindUpUtility.getCurrentDate());
        application.setProject(projectService.get(createdApplicationDTO.getProjectId()));
        application.setApplicant(userService.findByUserID(createdApplicationDTO.getUserId()).get());
        application.setJob(jobService.get(createdApplicationDTO.getJobId()));
        application.setStatus(ApplicationStatus.PENDING);
        application.setDescription(createdApplicationDTO.getDescription());
        application.setCreatedTimestamp(BindUpUtility.getCurrentTimestamp());

        return application;
    }
    @Operation(summary = "Create application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create application successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("")
    public ResponseEntity<ResponseObject> createApplication(@RequestBody CreatedApplicationDTO createdApplicationDTO) {
        boolean check = applicationService.findByProjectIdAndUserId(createdApplicationDTO.getUserId(), createdApplicationDTO.getProjectId());

        if (check == false) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "User already applied", ""));
        } else {
            Application application = fromCreatedApplicationDTOToApplication(createdApplicationDTO);
            application = applicationService.create(application);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Application added", application.getId())
            );
        }
    }

    @Operation(summary = "Change application status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Change application status successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PutMapping("/status")
    public ResponseEntity<ResponseObject> changeStatus(@RequestParam UUID applicationId, @RequestParam ApplicationStatus applicationStatus) {
        Application application = applicationService.changeStatus(applicationId, applicationStatus);

        if (applicationStatus == ApplicationStatus.ACCEPTED) {
            ProjectMember projectMember = new ProjectMember();
            projectMember.setTitle(application.getJob().getName());
            projectMember.setRole(application.getJob().getName());
            projectMember.setName(application.getApplicant().getName());
            projectMember.setProject(application.getProject());
            projectMemberService.create(projectMember);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Application status changed to " + applicationStatus.toString(), application.getId()));


//        String notificationTitle;
//        String notificationBody;
//
//        NotificationMessage notificationMessage = new NotificationMessage();
//        notificationMessage.setRecipientToken(application.getApplicant().getAccount().getDeviceToken());
//
//        Notification notification = new Notification();
//        notification.setLogo(application.getProject().getLogo());
//        notification.setCreatedTime(BindUpUtility.getCurrentTimestamp());
//        notification.setRecipient(application.getApplicant());

//        if (applicationStatus == ApplicationStatus.ACCEPTED) {

//
//            notificationTitle = "Your application has been accepted!";
//            notificationMessage.setTitle(notificationTitle);
//            notification.setTitle(notificationTitle);
//
//            notificationBody = "Your application in project " + application.getProject().getName() + " has been accepted!";
//            notificationMessage.setBody(notificationBody);
//            notification.setBody(notificationBody);
//        } else if(applicationStatus == ApplicationStatus.REJECTED) {
//            notificationTitle = "Your application has been rejected!";
//            notificationMessage.setTitle(notificationTitle);
//            notification.setTitle(notificationTitle);
//
//            notificationBody = "Your application in project " + application.getProject().getName() + " has been rejected!";
//            notificationMessage.setBody("Your application in project " + application.getProject().getName() + " has been rejected!");
//            notification.setBody(notificationBody);
//        } else {
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("OK", "Application status changed", ""));
//        }

//        Map<String, String> tmp = new HashMap<>();
//        notificationMessage.setData(tmp);
//        notificationMessage.setImage("");
//
//        firebaseMessagingService.sendNotificationByToken(notificationMessage);
//        notificationService.saveNotification(notification);
//
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK", "Application status changed to " + applicationStatus.toString(), applicationId));
    }
}
