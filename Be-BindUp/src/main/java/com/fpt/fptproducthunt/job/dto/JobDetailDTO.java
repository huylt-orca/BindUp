package com.fpt.fptproducthunt.job.dto;

import com.fpt.fptproducthunt.common.metadata.JobStatus;
import com.fpt.fptproducthunt.project.dto.ProjectDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDetailDTO {
    private UUID id;
    private String name;
    private String description;
    private Date dueDate;
    private JobStatus status;
    private UUID projectId;
    private String projectName;
    private String projectLogo;
}
