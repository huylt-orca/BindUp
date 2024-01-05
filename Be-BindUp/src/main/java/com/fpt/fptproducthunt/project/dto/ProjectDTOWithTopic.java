package com.fpt.fptproducthunt.project.dto;

import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import com.fpt.fptproducthunt.topic.dto.TopicDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTOWithTopic {
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
    List<TopicDTO> topicDTOList;
}
