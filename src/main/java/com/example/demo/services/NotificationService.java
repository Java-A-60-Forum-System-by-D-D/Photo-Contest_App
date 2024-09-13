package com.example.demo.services;

import com.example.demo.models.Notification;
import com.example.demo.models.NotificationType;
import com.example.demo.models.User;

public interface NotificationService {
    void markAllNotificationsAsRead(User user);
    void markNotificationAsRead(Notification notification);
    void sendNotification(String message, NotificationType type, User recipient);
}
