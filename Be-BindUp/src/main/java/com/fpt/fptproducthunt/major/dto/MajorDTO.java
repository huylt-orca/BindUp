package com.fpt.fptproducthunt.major.dto;

import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorDTO {
    private UUID id;
    private String name;
    private String description;
}
