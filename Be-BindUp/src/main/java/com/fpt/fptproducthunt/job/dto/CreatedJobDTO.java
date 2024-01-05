package com.fpt.fptproducthunt.job.dto;

import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
public class CreatedJobDTO {
    private String name;
    private String description;
    private UUID projectId;
    private Date dueDate;

}
