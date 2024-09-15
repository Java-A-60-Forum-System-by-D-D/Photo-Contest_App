package com.example.demo.controllers.mvc;


import com.example.demo.models.Notification;
import com.example.demo.models.User;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notifications")
public class NotificationController {


    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }
    @GetMapping("/unread")
    @ResponseBody
    public Map<String, List<Notification>> getUnreadNotifications(Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();
        List<Notification> notifications = notificationService.getUnreadNotifications(user);
        return Map.of("notifications", notifications);
    }

    @PostMapping("/markAsRead")
    public String markAsRead(@RequestParam Long notificationId) {
        Notification notification = notificationService.getNotificationById(notificationId);
        notificationService.markNotificationAsRead(notification);
        return "redirect:/";
    }
    @PostMapping("/markAllAsRead")
    public String markAllAsRead(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        notificationService.markAllNotificationsAsRead(user);
        return "redirect:/";
    }
}
