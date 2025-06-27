package com.marketplace.MarketBack.controller.dto;

import java.time.LocalDateTime;

public record ProfileResponseDTO(
        String username,
        String name,
        String lastName,
        String address,
        String phone,
        String email,
        String description,
        String avatarUrl,
        String portadaUrl,

        LocalDateTime createdAt,
        LocalDateTime updateAt
) {
}
