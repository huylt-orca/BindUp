package com.fpt.fptproducthunt.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTOList {
    List<ApplicationDetailDTO> applicationDTOList;
    private int numOfPages;
    private int pageSize;
}
