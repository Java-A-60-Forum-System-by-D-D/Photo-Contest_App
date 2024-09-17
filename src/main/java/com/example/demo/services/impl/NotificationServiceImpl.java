package com.example.demo.services.impl;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Notification;
import com.example.demo.models.NotificationType;
import com.example.demo.models.User;
import com.example.demo.repositories.NotificationRepository;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    public static final String NOTIFICATION_NOT_FOUND = "Notification not found";
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findAllByRecipientAndReadIsFalse(user);
    }

    @Override
    public Notification getNotificationById(long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(NOTIFICATION_NOT_FOUND));
    }

    @Override
    public void sendNotification(String message, NotificationType type, User recipient) {
        Notification notification = new Notification(message, type, recipient);
        notificationRepository.save(notification);
    }

    @Override
    public void senNotificationForContactForm(String message) {
        userService.findUsersByRoleOrganizer("ORGANIZER").stream()
                .forEach(u -> {
                            Notification notification = new Notification(message, NotificationType.OTHER, u);
                            notificationRepository.save(notification);
                        }
                );

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
