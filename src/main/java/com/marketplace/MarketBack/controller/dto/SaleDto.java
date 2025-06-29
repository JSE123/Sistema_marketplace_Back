package com.marketplace.MarketBack.controller.dto;

public record SaleDto(
    long productId,
    int amount,
    long total,
    long userId
) {
}
