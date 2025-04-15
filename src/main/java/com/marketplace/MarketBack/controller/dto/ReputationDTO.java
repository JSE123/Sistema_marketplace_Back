package com.marketplace.MarketBack.controller.dto;

public record ReputationDTO(
        int rating,
        String comment,
        Long userId
) {
}
