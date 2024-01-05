package com.fpt.fptproducthunt.job.dto;

import com.fpt.fptproducthunt.projectmember.dto.ProjectMemberDTO;
import lombok.Data;

import java.util.List;

@Data
public class JobDTOList {
    private List<JobDTO> jobDTOList;
    private int numOfPages;
    private int pageSize;
}
