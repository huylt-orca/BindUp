package com.fpt.fptproducthunt.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTOWithProjectList {
    private List<JobDTOWithProject> jobDTOWithProjectList;
    private int numOfPages;
    private int pageSize;


}
