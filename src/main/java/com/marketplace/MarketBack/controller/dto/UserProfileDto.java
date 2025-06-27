package com.marketplace.MarketBack.controller.dto;

public record UserProfileDto(
        String name,
        String lastName,
        String email,
        String description,
        String phone,
        String address
) {
}
