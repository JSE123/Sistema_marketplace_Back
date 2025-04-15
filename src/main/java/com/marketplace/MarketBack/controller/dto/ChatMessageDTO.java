package com.marketplace.MarketBack.controller.dto;

import java.time.LocalDateTime;

public record ChatMessageDTO(
        Long id,
        String content,
        String senderUsername,
        LocalDateTime timestamp
) {
}
