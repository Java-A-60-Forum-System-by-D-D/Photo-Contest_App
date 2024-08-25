package com.example.demo.repositories;

import com.example.demo.models.Category;
import com.example.demo.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository  extends JpaRepository<Notification,Long> {
}
