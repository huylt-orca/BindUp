package com.fpt.fptproducthunt.user.dto;

import com.fpt.fptproducthunt.common.entity.Application;
import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import com.fpt.fptproducthunt.common.metadata.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOWithApplication {
    private UUID id;
    private String name;
    private int gender; // 0 for man, 1 for woman
    private String headline;
    private String description;
    private String address;
    private String phone;
    private String email;
    private String avatar;
    private Role role;
    private UUID applicationId;
    private ApplicationStatus applicationStatus;
}
