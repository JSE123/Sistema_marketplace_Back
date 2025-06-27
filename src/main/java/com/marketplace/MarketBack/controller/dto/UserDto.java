package com.marketplace.MarketBack.controller.dto;

import com.marketplace.MarketBack.persistence.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record UserDto (

        Long id,
        String username,
        String name,
        String lastName,
        String email,
        String description,
        String phone,
        String address,
        boolean isEnable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Set<String> roles // Solo los nombres de los roles
){
    public static UserDto fromEntity(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getDescription(),
                user.getPhone(),
                user.getAddress(),
                user.isEnable(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getRoles().stream()
                        .map(role -> role.getRoleEnum().name())
                        .collect(Collectors.toSet())
        );
    }
}
