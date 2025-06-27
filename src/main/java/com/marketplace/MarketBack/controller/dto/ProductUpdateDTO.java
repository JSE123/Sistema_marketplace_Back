package com.marketplace.MarketBack.controller.dto;

public record ProductUpdateDTO(
        String title,
        String description,
        String location,
        long price,
        int stock

) {
}
