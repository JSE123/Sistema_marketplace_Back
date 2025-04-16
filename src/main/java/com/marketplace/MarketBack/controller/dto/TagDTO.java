package com.marketplace.MarketBack.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record TagDTO(
        @NotBlank String name,
        Long productId
) {
}
