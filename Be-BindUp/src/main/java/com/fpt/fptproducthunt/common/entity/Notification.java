package com.fpt.fptproducthunt.common.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(length = 512)
    private String logo;

    @Column(length = 100)
    @Size(min = 1)
    private String title;

    @Column(length = 512, columnDefinition = "text")
    @Size(min = 1)
    private String body;

    @Column(name = "createdTime", columnDefinition = "TIMESTAMP")
    private java.sql.Timestamp createdTime;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User recipient;
}
