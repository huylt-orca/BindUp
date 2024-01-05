package com.fpt.fptproducthunt.common.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fpt.fptproducthunt.common.metadata.ApplicationStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "projectid", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "jobid", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User applicant;

    @Column(columnDefinition = "text", length = 512)
    private String description;
    @Column(name = "createdDate", columnDefinition = "DATE")
    private java.sql.Date createdDate;

    @Column(name = "createdTimestamp", columnDefinition = "TIMESTAMP")
    private java.sql.Timestamp createdTimestamp;

    @Enumerated(EnumType.ORDINAL)
    private ApplicationStatus status;
}
