package com.fpt.fptproducthunt.changelog.dto;

import com.fpt.fptproducthunt.common.entity.Changelog;
import lombok.Data;

import java.util.List;

@Data
public class ChangelogDTOList {
    private List<Changelog> changelogList;
    private int numOfPages;
    private int pageSize;
}
