package com.fpt.fptproducthunt.common.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fpt.fptproducthunt.common.metadata.JobStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "projectid", nullable = false)
    private Project project;

    @Column(length = 100)
    @Size(min = 1, max = 100)
    private String name;
    private String description;
    @Column(columnDefinition = "DATE")
    private java.sql.Date dueDate;
    @Enumerated(EnumType.ORDINAL)
    private JobStatus jobStatus;
    @OneToMany(mappedBy="job", fetch = FetchType.LAZY)
    private List<Application> applications;
}
