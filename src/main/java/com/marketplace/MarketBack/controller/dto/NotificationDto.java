package com.marketplace.MarketBack.controller.dto;

import com.marketplace.MarketBack.persistence.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    Long id;
    String content;
    NotificationType type;
//    Long recipientId;
    LocalDateTime createdAt;
    boolean read;
}
