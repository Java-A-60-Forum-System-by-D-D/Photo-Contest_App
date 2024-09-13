package com.example.demo.repositories;

import com.example.demo.models.Category;
import com.example.demo.models.Notification;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("Select n from Notification n where n.recipient = ?1 and n.isRead = false")
    List<Notification> findAllByRecipientAndReadIsFalse(User recipient);
    List<Notification> findAllByRecipient(User recipient);
}
