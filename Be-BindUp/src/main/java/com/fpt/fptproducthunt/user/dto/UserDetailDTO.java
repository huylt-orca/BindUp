package com.fpt.fptproducthunt.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.fptproducthunt.account.dto.AccountDTO;
import com.fpt.fptproducthunt.application.dto.ApplicationDTO;
import com.fpt.fptproducthunt.common.entity.Account;
import com.fpt.fptproducthunt.common.entity.Application;
import com.fpt.fptproducthunt.common.entity.Major;
import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.metadata.Role;
import com.fpt.fptproducthunt.major.dto.MajorDTO;
import com.fpt.fptproducthunt.project.dto.ProjectDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO {
    private UUID id;
    private String name;
    private int gender; // 1 for man, 2 for woman
    private String headline;
    private String description;
    private String address;
    private String phone;
    private String email;
    private String avatar;
    private Role role;
    private AccountDTO account;
    private List<MajorDTO> majors;
    private List<ProjectDTO> votes;
    private List<ApplicationDTO> applications;
    private List<ProjectDTO> projects;
}
