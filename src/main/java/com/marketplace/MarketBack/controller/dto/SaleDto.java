package com.marketplace.MarketBack.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record SaleDto(
    long productId,
    int amount,
    long total,
    @NotBlank long userId
) {
}
