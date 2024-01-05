package com.fpt.fptproducthunt.projectmember.controller;

import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.ProjectMember;
import com.fpt.fptproducthunt.project.service.ProjectService;
import com.fpt.fptproducthunt.projectmember.dto.CreatedProjectMemberDTO;
import com.fpt.fptproducthunt.projectmember.dto.ProjectMemberDTO;
import com.fpt.fptproducthunt.projectmember.dto.ProjectMemberDTOList;
import com.fpt.fptproducthunt.projectmember.service.ProjectMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/members")
@CrossOrigin
@RestController
@Tag(name = "Project member APIs")
public class ProjectMemberController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProjectMemberService projectMemberService;
    @Autowired
    private ProjectService projectService;
    @Operation(summary = "Fetch project member list. paging = false means no paging and vice versa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query member list successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@RequestParam UUID projectId,
                                                 @RequestParam(defaultValue = "false") boolean paging,
                                                 @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                 @RequestParam(defaultValue = "10", required = false) int pageSize) {
        List<ProjectMember> projectMemberList = !paging ? projectMemberService.getAll(projectId) :
                projectMemberService.getAll(projectId, pageNo, pageSize, "createdDate").getContent();

        if (projectMemberList == null)
            projectMemberList = new ArrayList<ProjectMember>();

        ProjectMemberDTOList projectMemberDTOList = new ProjectMemberDTOList();
        List<ProjectMemberDTO> projectMemberDTOS = projectMemberList.stream().map(
                projectMember -> new ProjectMemberDTO(projectMember.getId(),
                        projectMember.getName(),
                        projectMember.getRole(),
                        projectMember.getTitle())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Project member list found", projectMemberDTOS)
        );
    }

    @Operation(summary = "Get project member by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query project member successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getProjectMemberById(@PathVariable UUID id) {
        Optional<ProjectMember> foundProjectMember = projectMemberService.get(id);

        return foundProjectMember.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Project member found", foundProjectMember)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Cannot find project member with id = " + id, "")
                );
    }

    private ProjectMember fromCreatedProjectMemberToProjectMember(CreatedProjectMemberDTO createdProjectMemberDTO) {
        this.modelMapper = new ModelMapper();

        TypeMap<CreatedProjectMemberDTO, ProjectMember> propertyMapper = this.modelMapper.createTypeMap(CreatedProjectMemberDTO.class, ProjectMember.class);
        propertyMapper.addMappings(mapper -> mapper.skip(ProjectMember::setProject));

        ProjectMember projectMember = modelMapper.map(createdProjectMemberDTO, ProjectMember.class);
        projectMember.setProject(projectService.get(createdProjectMemberDTO.getProjectId()));

        return projectMember;
    }
    @Operation(summary = "Create project member.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create project member successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("")
    public ResponseEntity<ResponseObject> createProjectMember(@RequestBody CreatedProjectMemberDTO createdProjectMemberDTO) {
        ProjectMember projectMember = fromCreatedProjectMemberToProjectMember(createdProjectMemberDTO);

        return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Project member found", projectMemberService.create(projectMember))
                );
    }
    @Operation(summary = "Update project member.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update project member successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PutMapping("")
    public ResponseEntity<ResponseObject> updateProjectMember(@RequestParam UUID id, String role, String title, String name) {
        ProjectMember foundProjectMember = projectMemberService.update(id, role, title, name);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Project member updated", foundProjectMember)
        );
    }

    @Operation(summary = "Delete project member by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete project member successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteProjectMember(@PathVariable UUID id) {
        projectMemberService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Project member deleted", "")
        );
    }
}
