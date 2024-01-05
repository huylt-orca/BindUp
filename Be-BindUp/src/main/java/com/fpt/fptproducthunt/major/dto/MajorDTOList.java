package com.fpt.fptproducthunt.major.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorDTOList {
    private List<MajorDTO> majorDTOList;
    private int noOfPages;
    private int pageSize;
}
