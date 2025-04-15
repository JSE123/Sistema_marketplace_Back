package com.marketplace.MarketBack.controller.dto;

import com.marketplace.MarketBack.persistence.entity.EstadoProducto;

public record ProductFilterDTO(
        String title,
        EstadoProducto status,
        Long categoryId,
        String location
) {
}
