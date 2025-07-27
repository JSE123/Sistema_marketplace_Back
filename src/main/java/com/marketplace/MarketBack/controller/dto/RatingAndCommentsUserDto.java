package com.marketplace.MarketBack.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class RatingAndCommentsUserDto {
    double averageRating;
    List<ResponseReputationDto> reputations;
}
