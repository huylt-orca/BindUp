package com.fpt.fptproducthunt.projectmember.dto;

import com.fpt.fptproducthunt.common.entity.Changelog;
import lombok.Data;

import java.util.List;

@Data
public class ProjectMemberDTOList {
    private List<ProjectMemberDTO> projectMemberDTOList;
    private int numOfPages;
    private int pageSize;
}
