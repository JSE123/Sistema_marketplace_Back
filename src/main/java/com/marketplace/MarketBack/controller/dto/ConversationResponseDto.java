package com.marketplace.MarketBack.controller.dto;

import com.marketplace.MarketBack.persistence.entity.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponseDto {
    private Long id;
    private List<MessageResponseDto> messages;
    private LocalDateTime lastUpdated;
    private int unreadCount;
}
