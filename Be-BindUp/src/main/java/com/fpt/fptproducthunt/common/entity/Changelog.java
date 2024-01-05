package com.fpt.fptproducthunt.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fpt.fptproducthunt.common.metadata.ChangelogStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
public class Changelog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "projectid", nullable = false)
    @JsonBackReference
    private Project project;
    @Column(name = "createdDate", columnDefinition = "DATE")
    private java.sql.Date createdDate;
    @Column(name = "createdTimestamp", columnDefinition = "TIMESTAMP")
    private java.sql.Timestamp createdTimestamp;
    @Column(length = 512, columnDefinition = "text")
    @Size(min = 30)
    private String description;
    @Column(length = 100)
    @Size(min = 10)
    private String title;
    @Enumerated(EnumType.ORDINAL)
    private ChangelogStatus changelogStatus;
}
