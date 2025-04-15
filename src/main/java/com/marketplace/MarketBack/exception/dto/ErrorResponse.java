package com.marketplace.MarketBack.exception.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErrorResponse(
        String message,
        String errorCode,
        HttpStatus status,
        LocalDateTime timestamp,
        List<ValidationError> details
) {
    @Builder
    public record ValidationError(String field, String error) {}
}
