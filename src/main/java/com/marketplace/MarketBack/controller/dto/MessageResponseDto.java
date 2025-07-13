package com.marketplace.MarketBack.controller.dto;

import com.marketplace.MarketBack.persistence.entity.MessageStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponseDto {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private Long senderId;
    private String senderName;
    private String senderUsername;
    private String senderAvatar;
    private Long recipientId;
    private String recipientName;
    private String recipientUsername;
    private MessageStatus status;
}
