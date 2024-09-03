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
@Entity(name = "users")
@DiscriminatorValue("USER")
public class User extends AuthUser implements UserDetails, GrantedAuthority {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "total_score")
    private Integer totalScore = 0;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "organizer",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Contest> organizedContests;


    @ManyToMany(mappedBy = "participants",fetch = FetchType.EAGER)
    private List<Contest> participatedContests;

    @ManyToMany(fetch = FetchType.EAGER)
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
