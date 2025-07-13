package com.marketplace.MarketBack.controller.dto;

import lombok.Data;

@Data
public class MessageRequestDto {
    private Long senderId;
    private Long recipientId;
    private String content;

}
