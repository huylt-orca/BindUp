package com.fpt.fptproducthunt.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectDTO {
    private String name;
    private String summary;
    private String description;
    private String source;
    private int milestone;
}
