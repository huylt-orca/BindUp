package com.fpt.fptproducthunt.changelog.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CreatedChangelogDTO {
    private String description;
    private String title;
    private UUID projectId;

}
