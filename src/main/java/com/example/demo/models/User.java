package com.example.demo.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @OneToOne
    @JoinColumn(name = "auth_user_id")
    private AuthUser authUser;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)

    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "organizer")
    private List<Contest> organizedContests;

    @ManyToMany(mappedBy = "participants")
    private List<Contest> participatedContests;

    @ManyToMany
    @JoinTable(
            name = "jury_contests",
            joinColumns = @JoinColumn(name = "jury_id"),
            inverseJoinColumns = @JoinColumn(name = "contest_id"))
    private List<Contest> jurorContests;



}
