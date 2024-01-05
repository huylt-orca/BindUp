package com.fpt.fptproducthunt.common.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Account {
    @Id
    @Column(name = "userid")
    @Type(type = "uuid-char")
    private UUID id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "userid")
    @JsonIgnore
    private User user;

    @Column(nullable = false, unique = true)
    @Size(min = 8, max = 40)
    private String username;
    @Column(nullable = false)
    @Size(min = 8, max = 40)
    private String password;
    private String deviceToken;
}
