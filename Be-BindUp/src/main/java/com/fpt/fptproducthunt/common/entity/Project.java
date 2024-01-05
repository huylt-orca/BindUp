package com.fpt.fptproducthunt.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity // This tells Hibernate to make a table out of this class
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(length = 100)
    @Size(min = 6, max = 100)
    private String name;

    @Column(length = 512, columnDefinition = "text")
    @Size(min = 30)
    private String summary;
    @Column(length = 512)
    private String logo;

    @Column(length = 512, columnDefinition = "text")
    @Size(min = 30, max = 512)
    private String description;
    @Column(length = 512)
    private String source;
    private int voteQuantity;
    private int milestone;
    private Date createdDate;
    @Enumerated(EnumType.ORDINAL)
    private ProjectStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "projectmentor",
            joinColumns = @JoinColumn(name = "projectID"),
            inverseJoinColumns = @JoinColumn(name = "mentorID"))
    List<Mentor> mentors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "vote",
            joinColumns = @JoinColumn(name = "projectID"),
            inverseJoinColumns = @JoinColumn(name = "userID"))
    List<User> votes;

    @OneToMany(mappedBy="project", fetch = FetchType.LAZY)
    private List<ProjectImage> images;

    @OneToMany(mappedBy="project", fetch = FetchType.LAZY)
    private List<ProjectMember> members;

    @OneToMany(mappedBy="project", fetch = FetchType.LAZY)
    private List<Job> jobs;

    @OneToMany(mappedBy="project", fetch = FetchType.LAZY)
    private List<Application> applications;

    @OneToMany(mappedBy="project", fetch = FetchType.LAZY)
    private List<Changelog> changelogs;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnore
    private User founder;

    @ManyToMany(mappedBy = "projects", fetch = FetchType.LAZY)
    List<Topic> topics;
}
