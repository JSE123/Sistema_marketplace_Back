package com.marketplace.MarketBack.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FeaturedSeller {
    private Long id;
    private String name;
    private String avatarUrl;
    private double rating;
}
