package com.fpt.fptproducthunt.projectmember.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberDTO {
    private UUID id;
    private String name;
    private String role;
    private String title;
}
