package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.NotificationDto;
import com.marketplace.MarketBack.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notifications")
public class NotificationContrller {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notify/{userId}")
    public void testNotification(@PathVariable Long userId) {
//        NotificationDto dto = new NotificationDto("Esto es una prueba");
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, "esto es una prueba");
    }

    @GetMapping("users/{userId}/notifications")
    public List<NotificationDto> getNotifications(@PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @PostMapping("users/{recipientId}/mark-all-as-read")
    public void markAsRead(@PathVariable Long recipientId) {
        notificationService.markNotificationsAsRead(recipientId);
    }
}
