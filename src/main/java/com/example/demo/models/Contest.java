package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "contests")
@Getter
@Setter
@NoArgsConstructor
public class Contest extends BaseEntity {

    @Column(name = "title", nullable = false, unique = true)
    private String title;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Type type;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private Phase phase = Phase.NOT_STARTED;

    @OneToMany(mappedBy = "contest", fetch = FetchType.EAGER)
    private List<PhotoSubmission> submissions;
    @ManyToOne
    private User organizer;
    @OneToMany
    @Transient
    private List<User> invitedUsers;
    @Column(name = "photo_url")
    private String photoUrl;



    @Column(name = "start_date", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_date")
    private LocalDateTime updatedAt;
    @Column(name = "start_phase_1")
    private LocalDateTime startPhase1;
    @Column(name = "start_phase_2")
    private LocalDateTime startPhase2;
    @Column(name = "start_phase_3")
    private LocalDateTime startPhase3;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "contest_participants",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> participants;


    public List<User> getInvitedUsers() {
        return type.equals(Type.OPEN) ? new ArrayList<>() : invitedUsers;
    }

    public void setInvitedUsers(List<User> invitedUsers) {
        if (type.equals(Type.INVITATIONAL)) {
            this.invitedUsers = invitedUsers;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contest contest = (Contest) o;
        return Objects.equals(title, contest.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }
}
