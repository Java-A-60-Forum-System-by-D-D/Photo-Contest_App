package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "contests")
@Getter
@Setter
@NoArgsConstructor
public class Contest extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_open", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isOpen;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Phase phase;
    @OneToMany(mappedBy = "contest")
    private List<PhotoSubmission> submissions;
    @ManyToOne
    private User organizer;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime updatedAt;
    @ManyToMany
    @JoinTable(
            name = "contest_participants",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> participants;



}
