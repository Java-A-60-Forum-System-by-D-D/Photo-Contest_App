package com.example.demo.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User recipient;

    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "type")
    private NotificationType type;

    public Notification() {
    }

    public Notification(String message, NotificationType type, User recipient) {
        this.message = message;
        this.type = type;
        this.recipient = recipient;
        this.createdAt = LocalDateTime.now();
    }
    public void markAsRead() {
        this.isRead = true;
    }
}

