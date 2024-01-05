package com.fpt.fptproducthunt.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJobDTO {
    private UUID id;
    private String name;
    private String description;
    private Date dueDate;
}
