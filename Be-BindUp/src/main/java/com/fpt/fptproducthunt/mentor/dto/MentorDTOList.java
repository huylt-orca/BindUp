package com.fpt.fptproducthunt.mentor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorDTOList {
    private List<MentorDTO> mentorDTOList;
    private int noOfPages;
    private int pageSize;
}
