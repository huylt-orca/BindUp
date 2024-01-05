package com.fpt.fptproducthunt.mentor.controller;

import com.fpt.fptproducthunt.common.dto.ErrorMessage;
import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.Mentor;
import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import com.fpt.fptproducthunt.mentor.dto.CreatedMentorDTO;
import com.fpt.fptproducthunt.mentor.dto.MentorDTO;
import com.fpt.fptproducthunt.mentor.dto.MentorDTOList;
import com.fpt.fptproducthunt.mentor.service.MentorService;
import com.fpt.fptproducthunt.project.dto.ProjectDTO;
import com.fpt.fptproducthunt.project.dto.ProjectListDTO;
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

@RequestMapping(value = "api/v1/mentors")
@CrossOrigin
@RestController
@Tag(name = "Mentor APIs")
public class MentorController {
    @Autowired
    public MentorService mentorService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Fetch mentor list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query user successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("")
    public ResponseEntity<ResponseObject> findAll(@RequestParam(defaultValue = "0") int pageNo,
                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy) {
        List<Mentor> mentorList= mentorService.findAll();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Mentor> mentorPaging = new PageImpl<>(mentorList, pageable, mentorList.size());

        List<MentorDTO> mentors = mentorPaging.hasContent() ? mentorPaging.getContent().stream().map(
                mentor -> new MentorDTO(
                        mentor.getId(),
                        mentor.getName(),
                        mentor.getMajor(),
                        mentor.getPhone(),
                        mentor.getEmail())).collect(Collectors.toList()) : new ArrayList<>();

        MentorDTOList mentorDTOList = new MentorDTOList();
        mentorDTOList.setMentorDTOList(mentors);
        mentorDTOList.setPageSize(mentors.size());
        mentorDTOList.setNoOfPages(mentorPaging.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Query mentor list successfully", mentorDTOList)
        );
    }

    @Operation(summary = "Fetch mentor list from project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query mentor list successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ResponseObject> findAllByProjectId(@PathVariable UUID projectId) {
        List<Mentor> mentorList = mentorService.findAllMentorByProjectId(projectId);
        List<MentorDTO> mentorDTOList = mentorList != null ? mentorList.stream().map(
                mentor -> new MentorDTO(
                        mentor.getId(),
                        mentor.getName(),
                        mentor.getMajor(),
                        mentor.getPhone(),
                        mentor.getEmail())).collect(Collectors.toList()) : new ArrayList<>();
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK",
                                              "Query mentor list successfully", mentorDTOList)
        );
    }

    @Operation(summary = "Get mentor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class))}),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class))}),
            @ApiResponse(responseCode = "404", description = "Cannot find mentor with ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)) })})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById(@PathVariable UUID id) {
        Mentor mentor = mentorService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Mentor found", mentor));
    }

    private Mentor fromCreatedMentorDTOToMentor(CreatedMentorDTO createdMentorDTO) {
        this.modelMapper = new ModelMapper();
        return modelMapper.map(createdMentorDTO, Mentor.class);
    }

    @Operation(summary = "Get project by mentor id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get projects by mentor id successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/{id}/projects")
    public ResponseEntity<ResponseObject> getProjectsByMentor(@PathVariable UUID id,
                                                             @RequestParam(defaultValue = "0") int pageNo,
                                                             @RequestParam(defaultValue = "10") int pageSize,
                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                             @RequestParam(defaultValue = "ASC") String ascending,
                                                             @RequestParam(defaultValue = "") String nameKeyword) {
        Mentor mentor = mentorService.findById(id);
        List<Project> projectList = new ArrayList<>();
        List<Project> tmpProjects = mentor.getProjects();
        for(int i = 0;i < tmpProjects.size();++i) {
            if (tmpProjects.get(i).getName().toLowerCase().contains(nameKeyword.toLowerCase())) {
                if (tmpProjects.get(i).getStatus() != ProjectStatus.DELETED)
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

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Found projects belong to mentor " + mentor.getName(), projectDTOList));
    }

    @Operation(summary = "Add mentor")
    @PostMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    public ResponseEntity<ResponseObject> addMentor(@RequestBody CreatedMentorDTO createdMentorDTO) {
        Mentor mentor = fromCreatedMentorDTOToMentor(createdMentorDTO);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Add mentor successfully", mentorService.addMentor(mentor))
        );
    }

    @Operation(summary = "Update mentor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update mentor successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "404", description = "Cannot find mentor with ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)) })})
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateMentor(@PathVariable UUID id, @RequestBody Mentor mentor) {
        Mentor updatedMentor = mentorService.updateMentor(id, mentor);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("OK", "Mentor updated", updatedMentor));
    }

    @Operation(summary = "Delete mentor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete mentor successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "404", description = "Cannot find mentor with ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)) })})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteMentor(@PathVariable UUID id) {
        mentorService.deleteMentor(id);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Mentor deleted", ""));
    }
}
