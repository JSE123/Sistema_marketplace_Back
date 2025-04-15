package com.marketplace.MarketBack.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
        @NotBlank String name
) {
}
