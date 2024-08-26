package com.example.demo.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@DiscriminatorValue("USER")
public class User extends AuthUser implements UserDetails, GrantedAuthority {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "total_score")
    private int totalScore;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "organizer")
    @JsonIgnore
    private List<Contest> organizedContests;

    @JsonIgnore
    @ManyToMany(mappedBy = "participants")
    private List<Contest> participatedContests;

    public User() {
        this.role=UserRole.JUNKIE;
    }

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "jury_contests",
            joinColumns = @JoinColumn(name = "jury_id"),
            inverseJoinColumns = @JoinColumn(name = "contest_id"))
    private List<Contest> jurorContests;


    @Override
    public String getAuthority() {
        return role.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(getRole());
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
