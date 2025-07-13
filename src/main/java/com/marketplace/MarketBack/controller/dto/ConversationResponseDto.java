package com.marketplace.MarketBack.controller.dto;

import com.marketplace.MarketBack.persistence.entity.MessageEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
@Data
public class ConversationResponseDto {
    private Long id;
    private List<MessageResponseDto> messages;
    private LocalDateTime lastUpdated;
}
