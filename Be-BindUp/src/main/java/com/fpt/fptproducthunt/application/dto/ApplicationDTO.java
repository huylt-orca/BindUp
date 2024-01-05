package com.fpt.fptproducthunt.application.dto;

import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {
    private UUID id;
    private String description;
    private java.sql.Date createdDate;
    private ApplicationStatus applicationStatus;

    private String createdTimestamp;
}
