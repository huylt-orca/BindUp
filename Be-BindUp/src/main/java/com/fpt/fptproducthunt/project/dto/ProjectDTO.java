package com.fpt.fptproducthunt.project.dto;

import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private UUID id;
    private String name;
    private String logo;
    private String summary;
    private String description;
    private String source;
    private int voteQuantity;
    private int milestone;
    private Date createdDate;
    private ProjectStatus status;
}
