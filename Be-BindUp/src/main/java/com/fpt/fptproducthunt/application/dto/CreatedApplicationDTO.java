package com.fpt.fptproducthunt.application.dto;

import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedApplicationDTO {
    private String description;
    private UUID projectId;
    private UUID jobId;
    private UUID userId;
}
