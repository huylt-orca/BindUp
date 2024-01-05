package com.fpt.fptproducthunt.projectimage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectImageDTO {
    private UUID id;
    private String directory;
}
