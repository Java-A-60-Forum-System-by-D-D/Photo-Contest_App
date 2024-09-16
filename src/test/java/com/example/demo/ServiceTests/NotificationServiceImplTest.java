package com.example.demo.ServiceTests;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Notification;
import com.example.demo.models.NotificationType;
import com.example.demo.models.User;
import com.example.demo.repositories.NotificationRepository;
import com.example.demo.services.impl.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void getUnreadNotificationsReturnsListOfUnreadNotifications() {
        User user = new User();
        List<Notification> notifications = List.of(new Notification(), new Notification());
        when(notificationRepository.findAllByRecipientAndReadIsFalse(user)).thenReturn(notifications);

        List<Notification> result = notificationService.getUnreadNotifications(user);

        assertEquals(2, result.size());
    }

    @Test
    void getUnreadNotificationsReturnsEmptyListWhenNoUnreadNotificationsExist() {
        User user = new User();
        when(notificationRepository.findAllByRecipientAndReadIsFalse(user)).thenReturn(List.of());

        List<Notification> result = notificationService.getUnreadNotifications(user);

        assertTrue(result.isEmpty());
    }

    @Test
    void getNotificationByIdReturnsNotification() {
        Notification notification = new Notification();
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        Notification result = notificationService.getNotificationById(1L);

        assertNotNull(result);
    }

    @Test
    void getNotificationByIdThrowsEntityNotFoundExceptionWhenNotificationDoesNotExist() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> notificationService.getNotificationById(1L));
    }

    @Test
    void sendNotificationSavesNotification() {
        User recipient = new User();
        NotificationType type = NotificationType.NEW_MESSAGE;
        String message = "Test message";

        notificationService.sendNotification(message, type, recipient);

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void markNotificationAsReadUpdatesNotification() {
        Notification notification = new Notification();
        notification.setRead(false);

        notificationService.markNotificationAsRead(notification);

        assertTrue(notification.isRead());
        verify(notificationRepository).save(notification);
    }

    @Test
    void markAllNotificationsAsReadUpdatesAllUnreadNotifications() {
        User user = new User();
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        notification1.setRead(false);
        notification2.setRead(false);
        List<Notification> notifications = List.of(notification1, notification2);
        when(notificationRepository.findAllByRecipientAndReadIsFalse(user)).thenReturn(notifications);

        notificationService.markAllNotificationsAsRead(user);

        assertTrue(notification1.isRead());
        assertTrue(notification2.isRead());
        verify(notificationRepository).saveAll(notifications);
    }
}