package com.fpt.fptproducthunt.application.dto;

import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import com.fpt.fptproducthunt.job.dto.JobDTO;
import com.fpt.fptproducthunt.project.dto.ProjectDTO;
import com.fpt.fptproducthunt.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDetailDTO {
    private UUID id;
    private Date createdDate;
    private String description;
    private ApplicationStatus applicationStatus;
    private UserDTO userDTO;
    private JobDTO jobDTO;
    private ProjectDTO projectDTO;

    private String createdTimestamp;
}
