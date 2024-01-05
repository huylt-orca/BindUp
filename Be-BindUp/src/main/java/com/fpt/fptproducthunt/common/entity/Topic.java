package com.fpt.fptproducthunt.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(length = 100, unique = true)
    @Size(min = 1)
    private String name;
    @Column(length = 512, columnDefinition = "text")
    @Size(min = 30)
    private String description;
    @Column(length = 10, unique = true)
    @Size(min = 1)
    private String shortName;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ProjectTopic",
            joinColumns = @JoinColumn(name = "topicID"),
            inverseJoinColumns = @JoinColumn(name = "projectID"))
    List<Project> projects;
}
