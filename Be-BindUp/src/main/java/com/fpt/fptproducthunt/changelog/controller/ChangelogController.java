package com.fpt.fptproducthunt.changelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.fptproducthunt.changelog.dto.ChangelogDTO;
import com.fpt.fptproducthunt.changelog.dto.ChangelogDTOList;
import com.fpt.fptproducthunt.changelog.dto.CreatedChangelogDTO;
import com.fpt.fptproducthunt.changelog.service.ChangelogService;
import com.fpt.fptproducthunt.cloudmessaging.service.FirebaseMessagingService;
import com.fpt.fptproducthunt.common.dto.NotificationMessage;
import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.Changelog;
import com.fpt.fptproducthunt.common.entity.Notification;
import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.metadata.ChangelogStatus;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import com.fpt.fptproducthunt.notification.service.NotificationService;
import com.fpt.fptproducthunt.project.dto.CreatedProjectDTO;
import com.fpt.fptproducthunt.project.dto.ProjectDTO;
import com.fpt.fptproducthunt.project.service.ProjectService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping(value = "api/v1/changelogs")
@CrossOrigin
@RestController
@Tag(name = "Changelog APIs")
public class ChangelogController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ChangelogService changelogService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Autowired
    private ProjectService projectService;
    @Operation(summary = "Fetch changelog list. paging = false means no paging and vice versa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query changelog list successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@RequestParam UUID projectId,
                                                 @RequestParam(defaultValue = "false") boolean paging,
                                                 @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                 @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                 @RequestParam(defaultValue = "created_timestamp", required = false) String sortBy,
                                                 @RequestParam(defaultValue = "DESC", required = true) String ascending,
                                                 @RequestParam(defaultValue = "", required = false) ChangelogStatus changelogStatus) {
        List<Changelog> changelogList = !paging ? changelogService.getAll(projectId) :
                changelogService.getAll(projectId, pageNo, pageSize, sortBy, ascending, changelogStatus).getContent();

        if (changelogList == null)
            changelogList = new ArrayList<Changelog>();

        ChangelogDTOList changelogDTOList = new ChangelogDTOList();
        List<ChangelogDTO> changelogDTOS = changelogList.stream().map(
                changelog -> new ChangelogDTO(changelog.getId(),
                        changelog.getTitle(),
                        changelog.getDescription(),
                        changelog.getChangelogStatus(),
                        changelog.getCreatedDate(),
                        changelog.getCreatedTimestamp().toString())).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Changelog list found", changelogDTOS)
        );
    }

    @Operation(summary = "Get changelog by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query changelog successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getChangelogById(@PathVariable UUID id) {
        Optional<Changelog> foundChangelog = changelogService.get(id);
        Changelog changelog = foundChangelog.get();
        ChangelogDTO changelogDTO = new ChangelogDTO(changelog.getId(), changelog.getTitle(), changelog.getDescription(), changelog.getChangelogStatus(),
                changelog.getCreatedDate(), changelog.getCreatedTimestamp().toString());

        return foundChangelog.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Changelog found", changelogDTO)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Cannot find changelog with id = " + id, "")
                );
    }

    private Changelog fromCreatedChanglogDTOToChangelog(CreatedChangelogDTO createdChangelogDTO) {
        this.modelMapper = new ModelMapper();

        TypeMap<CreatedChangelogDTO, Changelog> propertyMapper = this.modelMapper.createTypeMap(CreatedChangelogDTO.class, Changelog.class);
        propertyMapper.addMappings(mapper -> mapper.skip(Changelog::setProject));

        Changelog changelog = modelMapper.map(createdChangelogDTO, Changelog.class);

        changelog.setProject(projectService.get(createdChangelogDTO.getProjectId()));
        changelog.setCreatedDate(BindUpUtility.getCurrentDate());
        changelog.setCreatedTimestamp(BindUpUtility.getCurrentTimestamp());
        changelog.setChangelogStatus(ChangelogStatus.PUBLIC);

        return changelog;
    }
    @Operation(summary = "Create changelog.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create changelog successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("")
    public ResponseEntity<ResponseObject> createChangelog(@RequestBody CreatedChangelogDTO createdChangelogDTO) {
        Changelog createdChangelog = fromCreatedChanglogDTOToChangelog(createdChangelogDTO);

        return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Changelog created", changelogService.create(createdChangelog))
                );
    }
    @Operation(summary = "Update changelog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update changelog successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PutMapping("")
    public ResponseEntity<ResponseObject> updateChangelog(@RequestParam UUID id, String title, String description) {
        Changelog foundChangelog = changelogService.update(id, title, description);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Changelog updated", foundChangelog)
        );
    }
    @Operation(summary = "Delete changelog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete changelog successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @DeleteMapping("")
    public ResponseEntity<ResponseObject> deleteChangelog(@RequestParam UUID id) {
        changelogService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Changelog deleted", ""));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseObject> changeStatus(@PathVariable UUID id, @RequestParam ChangelogStatus status) {
        Changelog changelog = changelogService.changeStatus(id, status);

        String notificationTitle;
        String notificationBody;

        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setRecipientToken(changelog.getProject().getFounder().getAccount().getDeviceToken());

        Notification notification = new Notification();
        notification.setLogo(changelog.getProject().getLogo());
        notification.setCreatedTime(BindUpUtility.getCurrentTimestamp());
        notification.setRecipient(changelog.getProject().getFounder());

        if(status == ChangelogStatus.PUBLIC) {
            notificationBody = "Admin changed your changelog " + changelog.getTitle() + " to public!";
            notificationMessage.setBody(notificationBody);
            notification.setBody(notificationBody);
        }
        if(status == ChangelogStatus.PRIVATE) {
            notificationBody = "Admin changed your changelog " + changelog.getTitle() + " to private!";
            notificationMessage.setBody(notificationBody);
            notification.setBody(notificationBody);
        }
        notificationTitle = "Your changelog in project " + changelog.getProject().getName() + " has changed status";
        notificationMessage.setTitle(notificationTitle);
        notification.setTitle(notificationTitle);

        Map<String, String> tmp = new HashMap<>();

        notificationMessage.setData(tmp);
        notificationMessage.setImage("");

        firebaseMessagingService.sendNotificationByToken(notificationMessage);
        notificationService.saveNotification(notification);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Changelog status changed", changelog.getId()));
    }
}
