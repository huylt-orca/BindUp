package com.fpt.fptproducthunt.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectListDTO {
    private List<ProjectDTO> projectDTOList;

    private int numOfPages;
    private int pageSize;
}
