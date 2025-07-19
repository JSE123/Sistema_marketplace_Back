package com.marketplace.MarketBack.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseReputationDto {
    long id;
    Long productId;
    int rating;
    String comment;
    String raterUserName;
    String raterUserUsername;
    String raterUserAvatar;
}
