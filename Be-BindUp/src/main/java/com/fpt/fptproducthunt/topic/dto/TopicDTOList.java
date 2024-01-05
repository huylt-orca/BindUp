package com.fpt.fptproducthunt.topic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTOList {
    private List<TopicDTO> topicDTOList;
    private int noOfPages;
    private int pageSize;
}
