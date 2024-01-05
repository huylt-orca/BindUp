package com.fpt.fptproducthunt.project.dto;

import lombok.*;

import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatedProjectDTO {
    private String name;
    private String summary;
    private String description;
    private String source;
    private int voteQuantity;
    private int milestone;
    private UUID founderId;
}
