package com.fpt.fptproducthunt.major.controller;

import com.fpt.fptproducthunt.common.dto.ErrorMessage;
import com.fpt.fptproducthunt.common.dto.ResponseObject;
import com.fpt.fptproducthunt.common.entity.Major;
import com.fpt.fptproducthunt.major.dto.CreatedMajorDTO;
import com.fpt.fptproducthunt.major.dto.MajorDTO;
import com.fpt.fptproducthunt.major.dto.MajorDTOList;
import com.fpt.fptproducthunt.major.service.MajorService;
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
@RequestMapping(value = "api/v1/majors")
@Tag(name = "Major APIs")
public class MajorController {
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private MajorService majorService;

    @Operation(summary = "Fetch major list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query major list successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@RequestParam(defaultValue = "0") int pageNo,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @RequestParam(defaultValue = "id") String sortBy) {
        List<Major> majorList = majorService.getAll();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Major> majorPaging = new PageImpl<>(majorList, pageable, majorList.size());

        List<MajorDTO> majors = majorPaging.hasContent() ? majorPaging.getContent().stream().map(
                major -> new MajorDTO(
                        major.getId(),
                        major.getName(),
                        major.getDescription())).collect(Collectors.toList()) : new ArrayList<>();
        MajorDTOList majorDTOList = new MajorDTOList();
        majorDTOList.setMajorDTOList(majors);
        majorDTOList.setPageSize(majors.size());
        majorDTOList.setNoOfPages(majorPaging.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Major list found", majorDTOList));
    }

    @Operation(summary = "Fetch major by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query major successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "404", description = "Cannot find major with ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)) })})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getMajorById(@PathVariable UUID id) {
        Major majorFetchResult = majorService.get(id);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Major found", majorFetchResult));
    }
    
    private Major fromCreatedMajorDTOToMajor(CreatedMajorDTO createdMajorDTO) {
        this.modelMapper = new ModelMapper();
        return modelMapper.map(createdMajorDTO, Major.class);
    }

    @Operation(summary = "Create major")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create major successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) })})
    @PostMapping("")
    public ResponseEntity<ResponseObject> addMajor(@RequestBody CreatedMajorDTO createdMajorDTO) {
        Major createdMajor = fromCreatedMajorDTOToMajor(createdMajorDTO);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Major created", majorService.createMajor(createdMajor)));
    }

    @Operation(summary = "Update major")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update major successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "404", description = "Cannot find major with ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)) })})
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateMajor(@PathVariable UUID id, @RequestBody Major major) {
        Major updatedMajor = majorService.updateMajor(id, major);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Major updated", updatedMajor));
    }

    @Operation(summary = "Delete major by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete major successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "403", description = "Don't have permission",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class)) }),
            @ApiResponse(responseCode = "404", description = "Cannot find major with ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)) })})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteMajor(@PathVariable UUID id) {
        majorService.deleteMajor(id);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseObject("OK", "Major deleted", ""));
    }
}
