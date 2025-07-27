package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.CreateReputationDto;
import com.marketplace.MarketBack.service.ReputationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reputation")
public class ReputationController {
    @Autowired
    private ReputationService reputationService;

    // Endpoint to create a new reputation entry
    @PostMapping
    public ResponseEntity<?> createReputation(@RequestBody CreateReputationDto createReputationDto, Authentication auth){
        return ResponseEntity.ok(reputationService.createReputation(createReputationDto.getProductId(), createReputationDto.getRating(), auth, createReputationDto.getComment()));
    }

    // Endpoint to get the rating and comments
    @GetMapping("/{productId}")
    public ResponseEntity<?> getReputations(@PathVariable Long productId){
        return ResponseEntity.ok(reputationService.getReputation(productId));
    }

    @GetMapping("/featured-sellers")
    public ResponseEntity<?> getFeaturedSellers() {
        return ResponseEntity.ok(reputationService.getFeaturedSellers());
    }

    @GetMapping("/user-assessment/{userId}")
    public ResponseEntity<?> getUserRatingAndComments(@PathVariable Long userId) {
        return ResponseEntity.ok(reputationService.getUserRating(userId));
    }
}
