package com.example.demo.services.impl;

import com.example.demo.models.Notification;
import com.example.demo.models.NotificationType;
import com.example.demo.models.User;
import com.example.demo.repositories.NotificationRepository;
import com.example.demo.services.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findAllByRecipientAndReadIsFalse(user);
    }

    @Override
    public void sendNotification(String message, NotificationType type, User recipient) {
        Notification notification = new Notification(message, type, recipient);
        notificationRepository.save(notification);
    }

    @Override

    public void markNotificationAsRead(Notification notification) {
        notification.markAsRead();
        notificationRepository.save(notification);
    }

    @Override
    public void markAllNotificationsAsRead(User user) {
        List<Notification> unreadNotifications = getUnreadNotifications(user);
        for (Notification notification : unreadNotifications) {
            notification.markAsRead();
        }
        notificationRepository.saveAll(unreadNotifications);
    }
}
