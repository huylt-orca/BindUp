package com.fpt.fptproducthunt.topic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatedTopicDTO {
    private String name;
    private String description;
    private String shortName;
}
