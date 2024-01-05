package com.fpt.fptproducthunt.mentor.dto;

import com.fpt.fptproducthunt.common.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorDTO {
    private UUID id;
    private String name;
    private String major;
    private String phone;
    private String email;
}
