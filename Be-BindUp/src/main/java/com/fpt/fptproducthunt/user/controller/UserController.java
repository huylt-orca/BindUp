package com.fpt.fptproducthunt.user.controller;

import com.fpt.fptproducthunt.account.dto.AccountDTO;
import com.fpt.fptproducthunt.application.dto.ApplicationDTO;
import com.fpt.fptproducthunt.application.service.ApplicationService;
import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.Application;
import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.entity.User;
import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import com.fpt.fptproducthunt.firebasestore.FirebaseService;
import com.fpt.fptproducthunt.major.dto.MajorDTO;
import com.fpt.fptproducthunt.project.dto.ProjectDTO;
import com.fpt.fptproducthunt.project.service.ProjectService;
import com.fpt.fptproducthunt.user.dto.UserDTO;
import com.fpt.fptproducthunt.user.dto.UserDetailDTO;
import com.fpt.fptproducthunt.user.dto.UserUpdateDTO;
import com.fpt.fptproducthunt.user.service.UserService;
import com.fpt.fptproducthunt.utility.BindUpUtility;
import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping(value = "api/v1/users")
@RestController
@CrossOrigin
@Tag(name = "User APIs")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private FirebaseService firebaseService;

    private UserDetailDTO convertUserToUserDetailDTO(User user) {
        this.modelMapper = new ModelMapper();

        UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);
        UserDetailDTO userDetailDTO = this.modelMapper.map(userDTO, UserDetailDTO.class);
        userDetailDTO.setAccount(this.modelMapper.map(user.getAccount(), AccountDTO.class));
        userDetailDTO.setMajors(BindUpUtility.mapList(user.getMajors(), MajorDTO.class));
        userDetailDTO.setVotes(BindUpUtility.mapList(user.getMyVotes(), ProjectDTO.class));
        userDetailDTO.setApplications(BindUpUtility.mapList(user.getApplications(), ApplicationDTO.class));
        userDetailDTO.setProjects(BindUpUtility.mapList(user.getProjects(), ProjectDTO.class));

        return userDetailDTO;
    }
    @Operation(summary = "Find user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query user successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findByUserID(@PathVariable UUID id) {
        Optional<User> user = userService.findByUserID(id);

        return user.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "User found", convertUserToUserDetailDTO(user.get())))
                :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Cannot find user with id = "+ id, "")
                );
    }

    @Operation(summary = "Find projects by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query user successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/{id}/projects")
    public ResponseEntity<ResponseObject> findUserProjects(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Found projects", projectService.getAllByUserId(id)));
    }

    @Operation(summary = "Find user's joined projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query project list successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/{id}/members")
    public ResponseEntity<ResponseObject> findJoinedProjects(@PathVariable UUID id) {
        List<Application> applicationList =
                applicationService.getAllByUserId(id, 0, 999, "created_timestamp", "ASC", 1).getContent();
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        for (Application application: applicationList) {
            Project project = application.getProject();
            projectDTOList.add(new ProjectDTO(
                    project.getId(),
                    project.getName(),
                    project.getLogo(),
                    project.getSummary(),
                    project.getDescription(),
                    project.getSource(),
                    project.getVoteQuantity(),
                    project.getMilestone(),
                    project.getCreatedDate(),
                    project.getStatus()
            ));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Found projects", projectDTOList));
    }

    @Operation(summary = "Update user information. gender (0, 1) => (men, women)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update user information successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable UUID id,
                                                 @RequestBody UserUpdateDTO userUpdateDTO) {
        User user = userService.findByUserID(id).get();
        user.setName(userUpdateDTO.getName());
        user.setGender(userUpdateDTO.getGender());
        user.setHeadline(userUpdateDTO.getHeadline());
        user.setDescription(userUpdateDTO.getDescription());
        user.setAddress(userUpdateDTO.getAddress());
        user.setPhone(userUpdateDTO.getPhone());

        user = userService.update(user);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Updated user", user.getUserId()));
    }

    @Operation(summary = "Upload logo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload image successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PutMapping(value="/{userId}/avatar",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> uploadAvatar(@PathVariable UUID userId, @RequestPart(required = true) MultipartFile imageFile) throws FirebaseAuthException {
        try {
//            log.info("Calling firebase service");
            String fileName = firebaseService.uploadImage(imageFile);
            userService.uploadAvatar(userId, fileName);
//            log.info("Returning response with image URL");

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Link image = " + fileName, fileName)
            );
        } catch (IOException e) {
//            log.info(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
