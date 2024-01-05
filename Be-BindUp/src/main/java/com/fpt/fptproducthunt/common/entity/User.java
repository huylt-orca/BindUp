package com.fpt.fptproducthunt.common.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fpt.fptproducthunt.common.custom.BindUpUserDetails;
import com.fpt.fptproducthunt.common.metadata.Role;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User implements BindUpUserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(length = 50, nullable = false)
    @Size(min = 6, max = 50)
    private String name;
    private int gender; // 1 for man, 2 for woman
    @Column(length = 100)
    private String headline;
    @Column(length = 512, columnDefinition = "text")
    private String description;
    @Column(length = 512)
    private String address;
    @Column(length = 20, nullable = true, unique = true)
    private String phone;
    @Email
    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @Column(length = 250)
    private String avatar;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
//    @JsonIgnore
    private Account account;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usermajor",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "majorID"))
    List<Major> majors;

    @ManyToMany(mappedBy = "votes", fetch = FetchType.LAZY)
    List<Project> myVotes;

    @OneToMany(mappedBy="applicant", fetch = FetchType.LAZY)
    private List<Application> applications;

    @OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @OneToMany(mappedBy="founder", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Project> projects;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUserId() {return String.valueOf(account.getId());}

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
