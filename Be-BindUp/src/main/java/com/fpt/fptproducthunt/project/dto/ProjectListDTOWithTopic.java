package com.fpt.fptproducthunt.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectListDTOWithTopic {
    private List<ProjectDTOWithTopic> projectDTOWithTopicList;

    private int numOfPages;
    private int pageSize;
}
