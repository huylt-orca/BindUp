package com.fpt.fptproducthunt.job.dto;

import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import com.fpt.fptproducthunt.common.metadata.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private UUID id;
    private String name;
    private String description;
    private Date dueDate;
    private JobStatus status;
}
