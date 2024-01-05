package com.fpt.fptproducthunt.projectmember.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CreatedProjectMemberDTO {
    private String role;
    private String title;
    private String name;
    private UUID projectId;

}
