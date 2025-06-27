package com.marketplace.MarketBack.controller.dto;

import com.marketplace.MarketBack.persistence.entity.EstadoProducto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record   ProductDTO(
    @NotBlank String title,
    @NotBlank String description,
    @NotBlank long price,
    @NotBlank int stock,
    @Valid EstadoProducto status,
    Long categoryId,
    String location,
    Long id
) {
}
