package com.fpt.fptproducthunt.common.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;
    @Column(length = 50)
    @Size(min = 6)
    private String name;
    @Column(length = 100)
    @Size(min = 1)
    private String major;
    @Column(unique = true, length = 20)
    private String phone;
    @Column(unique = true, length = 50)
    private String email;

    @JsonIgnore
    @ManyToMany(mappedBy = "mentors", fetch = FetchType.LAZY)
    List<Project> projects;
}
