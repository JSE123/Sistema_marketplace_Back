package com.marketplace.MarketBack.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class MarkMessageDto {
    List<Long> messageIds;
    long userId;
}
