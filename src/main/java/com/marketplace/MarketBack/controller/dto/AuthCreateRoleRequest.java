package com.marketplace.MarketBack.controller.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record AuthCreateRoleRequest(
        @Size(max = 2, message = "The user cannot have more than 2 roles")
        List<String> roleListName
) {
}
