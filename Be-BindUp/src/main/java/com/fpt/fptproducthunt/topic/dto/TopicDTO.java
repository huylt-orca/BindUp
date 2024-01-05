package com.fpt.fptproducthunt.topic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {
    private UUID id;
    private String name;
    private String description;
    private String shortName;
}
