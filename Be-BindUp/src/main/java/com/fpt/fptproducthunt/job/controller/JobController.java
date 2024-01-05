package com.fpt.fptproducthunt.job.controller;

import com.fpt.fptproducthunt.application.service.ApplicationService;
import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.*;
import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import com.fpt.fptproducthunt.common.metadata.JobStatus;
import com.fpt.fptproducthunt.job.dto.*;
import com.fpt.fptproducthunt.job.service.JobService;
import com.fpt.fptproducthunt.project.dto.ProjectDTO;
import com.fpt.fptproducthunt.project.service.ProjectService;
import com.fpt.fptproducthunt.projectmember.dto.CreatedProjectMemberDTO;
import com.fpt.fptproducthunt.projectmember.dto.ProjectMemberDTO;
import com.fpt.fptproducthunt.projectmember.dto.ProjectMemberDTOList;
import com.fpt.fptproducthunt.user.dto.UserDTO;
import com.fpt.fptproducthunt.user.dto.UserDTOWithApplication;
import com.fpt.fptproducthunt.user.dto.UserDetailDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/jobs")
@CrossOrigin
@RestController
@Tag(name = "Job APIs")
public class JobController {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private JobService jobService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ProjectService projectService;

//    @Operation(summary = "Fetch job list. paging = false means no paging and vice versa")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Query project job successfully",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ResponseObject.class)) }),
//            @ApiResponse(responseCode = "403", description = "Don't have permission",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ResponseObject.class)) })})
//    @GetMapping("/")
//    public ResponseEntity<ResponseObject> getAll(@RequestParam UUID projectId,
//                                                 @RequestParam(defaultValue = "false") boolean paging,
//                                                 @RequestParam(defaultValue = "0", required = false) int pageNo,
//                                                 @RequestParam(defaultValue = "10", required = false) int pageSize) {
//        List<Job> jobList = jobService.getAll(projectId);
//
//        if (jobList == null)
//            jobList = new ArrayList<Job>();
//
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//        Page<Job> jobPaging = new PageImpl<>(jobList, pageable, jobList.size());
//
//        JobDTOList jobDTOList = new JobDTOList();
//        List<JobDTO> jobDTOS = jobList.stream().map(
//                job -> new JobDTO(job.getId(),
//                        job.getName(),
//                        job.getDescription())).collect(Collectors.toList());
//        jobDTOList.setJobDTOList(jobDTOS);
//        jobDTOList.setPageSize(pageSize);
//        jobDTOList.setNumOfPages(jobPaging.getTotalPages());
//
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK", "Job list found", jobDTOList)
//        );
//    }
    @Operation(summary = "Fetch job list. sort by id/name/due_date, ASC/DESC = Ascending/Descending, keyword when find by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query project job successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                 @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                 @RequestParam(defaultValue = "due_date", required = false) String sortBy,
                                                 @RequestParam(defaultValue = "ASC", required = true) String ascending,
                                                 @RequestParam(defaultValue = "", required = false) String keyword) {
        Page<Job> jobPage = jobService.getAll(pageNo, pageSize, sortBy, ascending, keyword);

        List<Job> jobList = jobPage.hasContent() ? jobPage.getContent() : new ArrayList<>();

        JobDTOWithProjectList jobDTOWithProjectList = new JobDTOWithProjectList(jobList.stream().map(
                job -> new JobDTOWithProject(job.getId(),
                        job.getName(),
                        job.getDescription(),
                        job.getDueDate(),
                        job.getJobStatus(),
                        job.getProject().getId(),
                        job.getProject().getName(),
                        job.getProject().getLogo())).collect(Collectors.toList()), jobList.size(), jobPage.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Job list found", jobDTOWithProjectList)
        );
    }

    @Operation(summary = "Fetch job list by projectId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query project job successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/{projectId}")
    public ResponseEntity<ResponseObject> getAll(@PathVariable UUID projectId) {
        List<Job> jobList = jobService.getAll(projectId);

//        List<JobDTO> jobDTOS = jobList.stream().map(
//                job -> new JobDTO(job.getId(),
//                        job.getName(),
//                        job.getDescription())).collect(Collectors.toList());
        List<JobDTOWithProject> jobDTOWithProjectList = new ArrayList<>();
        for (Job job: jobList)
            jobDTOWithProjectList.add(new JobDTOWithProject(job.getId(), job.getName(), job.getDescription(), job.getDueDate(), job.getJobStatus(),
                    job.getProject().getId(), job.getProject().getName(), job.getProject().getLogo()));


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Job list found", jobDTOWithProjectList)
        );
    }

    @Operation(summary = "Fetch user list by jobId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query user list successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/user")
    public ResponseEntity<ResponseObject> getAllUserApplied(@RequestParam(defaultValue = "") UUID jobId,
                                                            @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                            @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                            @RequestParam(defaultValue = "id", required = false) String sortBy,
                                                            @RequestParam(defaultValue = "ASC", required = true) String ascending,
                                                            @RequestParam(defaultValue = "", required = false) ApplicationStatus applicationStatus,
                                                            @RequestParam(defaultValue = "", required = false) String keyword) {
        if (applicationStatus != null)
            System.out.println(applicationStatus.ordinal());
        System.out.println(applicationStatus);
        Page<Application> applicationPage = applicationService.getAllByJobId(jobId, pageNo, pageSize, sortBy, ascending, applicationStatus, keyword);
        List<Application> applicationList = applicationPage.hasContent() ? applicationPage.getContent() : new ArrayList<>();

        List<UserDTOWithApplication> userDTOWithApplicationList  = new ArrayList<>();
        for (Application application: applicationList) {
            User user = application.getApplicant();
            userDTOWithApplicationList.add(new UserDTOWithApplication(user.getId(), user.getName(), user.getGender(), user.getHeadline(), user.getDescription(),
                    user.getAddress(), user.getPhone(), user.getEmail(), user.getAvatar(), user.getRole(), application.getId(), application.getStatus()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "User list found", userDTOWithApplicationList)
        );
    }

    @Operation(summary = "Get job by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query job successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/{id}/detail")
    public ResponseEntity<ResponseObject> getJobById(@PathVariable UUID id) {
        Job job = jobService.get(id);

        JobDetailDTO jobDetailDTO = new JobDetailDTO(job.getId(), job.getName(), job.getDescription(),
                job.getDueDate(), job.getJobStatus(), job.getProject().getId(), job.getProject().getName(), job.getProject().getLogo());

        return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Job found", jobDetailDTO)
                );
    }

    private Job fromCreatedJobToJob(CreatedJobDTO createdJobDTO) {
        this.modelMapper = new ModelMapper();

        TypeMap<CreatedJobDTO, Job> propertyMapper = this.modelMapper.createTypeMap(CreatedJobDTO.class, Job.class);
        propertyMapper.addMappings(mapper -> mapper.skip(Job::setProject));

        Job job = modelMapper.map(createdJobDTO, Job.class);
        job.setProject(projectService.get(createdJobDTO.getProjectId()));
        job.setJobStatus(JobStatus.OPENING);

        return job;
    }
    @Operation(summary = "Create job.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create job successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("")
    public ResponseEntity<ResponseObject> createJob(@RequestBody CreatedJobDTO createdJobDTO) {
        Job job = fromCreatedJobToJob(createdJobDTO);
        jobService.create(job);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Job added", job.getId())
        );
    }
    @Operation(summary = "Update job.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update job successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PutMapping("")
    public ResponseEntity<ResponseObject> updateJob(@RequestBody UpdateJobDTO updateJobDTO) {
        Job job = jobService.update(updateJobDTO.getId(), updateJobDTO.getName(), updateJobDTO.getDescription(), updateJobDTO.getDueDate());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Job updated", job.getId())
        );
    }

    @Operation(summary = "Delete job by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete job successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteJob(@PathVariable UUID id) {
        jobService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Job deleted", "")
        );
    }

    @Operation(summary = "Update job.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update job successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PutMapping("/{jobId}/status")
    public ResponseEntity<ResponseObject> updateJob(@PathVariable UUID jobId, @RequestParam JobStatus jobStatus) {
        Job job = jobService.changeStatus(jobId, jobStatus);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Job updated", job.getId())
        );
    }
}
