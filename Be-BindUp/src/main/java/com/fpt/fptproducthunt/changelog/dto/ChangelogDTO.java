package com.fpt.fptproducthunt.changelog.dto;

import com.fpt.fptproducthunt.common.metadata.ChangelogStatus;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangelogDTO {
    private UUID id;
    private String title;
    private String description;
    private ChangelogStatus changelogStatus;
    private Date createdDate;
    private String createdTimestamp;
}
