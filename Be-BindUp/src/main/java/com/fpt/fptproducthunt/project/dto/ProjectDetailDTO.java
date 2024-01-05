package com.fpt.fptproducthunt.project.dto;

import com.fpt.fptproducthunt.application.dto.ApplicationDTO;
import com.fpt.fptproducthunt.changelog.dto.ChangelogDTO;
import com.fpt.fptproducthunt.common.entity.Mentor;
import com.fpt.fptproducthunt.common.entity.ProjectImage;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import com.fpt.fptproducthunt.job.dto.JobDTO;
import com.fpt.fptproducthunt.mentor.dto.MentorDTO;
import com.fpt.fptproducthunt.projectimage.dto.ProjectImageDTO;
import com.fpt.fptproducthunt.projectmember.dto.ProjectMemberDTO;
import com.fpt.fptproducthunt.topic.dto.TopicDTO;
import com.fpt.fptproducthunt.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetailDTO {
    private UUID id;
    private String name;
    private String logo;
    private String summary;
    private String description;
    private String source;
    private int voteQuantity;
    private int milestone;
    private Date createdDate;
    private ProjectStatus status;

    List<MentorDTO> mentors;
    // no need for voteDTO
    List<ProjectImageDTO> images;
    List<ProjectMemberDTO> members;
    List<JobDTO> jobs;
    List<ApplicationDTO> applications;
    List<ChangelogDTO> changelogs;
    UserDTO founder;
    List<TopicDTO> topics;

}
