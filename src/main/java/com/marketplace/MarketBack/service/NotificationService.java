package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.controller.dto.NotificationDto;
import com.marketplace.MarketBack.exception.custom.NotFoundException;
import com.marketplace.MarketBack.exception.enums.ErrorCode;
import com.marketplace.MarketBack.persistence.entity.NotificationEntity;
import com.marketplace.MarketBack.persistence.entity.NotificationType;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.repository.NotificationRepository;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // Method to create a notification
    public void createNotification(String content, long recipientId, NotificationType type) {
        UserEntity userRecipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new NotFoundException("User not found", ErrorCode.USER_NOT_FOUND));

        // Create a new notification entity
        NotificationEntity notification = NotificationEntity.builder()
                .content(content)
                .type(type)
                .recipient(userRecipient)
                .build();
        notificationRepository.save(notification);


    }

    public void sendNotificationToUser(String content, long recipientId, NotificationType type) {
//        simpMessagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);

        UserEntity user = userRepository.findById(recipientId)
                .orElseThrow(() -> new NotFoundException("User not found", ErrorCode.USER_NOT_FOUND));
        // 1. Guardar en base de datos
        NotificationEntity entity = new NotificationEntity();
        entity.setRecipient(user);
        entity.setContent(content);
        entity.setType(type);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setRead(false);

        NotificationEntity saved = notificationRepository.save(entity);

        // 2. Convertir a DTO
        NotificationDto dto = new NotificationDto();
        dto.setId(saved.getId());
        dto.setContent(saved.getContent());
        dto.setType(saved.getType());
        dto.setCreatedAt(saved.getCreatedAt());
        dto.setRead(saved.isRead());

        // 3. Enviar por WebSocket
        simpMessagingTemplate.convertAndSend("/topic/notifications/" + user.getId(), dto);
    }

    public List<NotificationDto> getNotificationsForUser(long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found", ErrorCode.USER_NOT_FOUND));

        List<NotificationEntity> notifications = notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
        return notifications.stream()
                .map(notification -> new NotificationDto(
                        notification.getId(),
                        notification.getContent(),
                        notification.getType(),
                        notification.getCreatedAt(),
                        notification.isRead()))
                .toList();
    }


    // mark the notifications as read
    public void markNotificationsAsRead(long recipientId) {
        List<NotificationEntity> notification = notificationRepository.findByRecipientId(recipientId)
                .orElseThrow(() -> new NotFoundException("Notification not found", ErrorCode.NOTIFICATION_NOT_FOUND));

        // Mark all notifications as read
        for (NotificationEntity notificationEntity : notification) {
            notificationEntity.setRead(true);
            notificationRepository.save(notificationEntity);
        }

    }
}
