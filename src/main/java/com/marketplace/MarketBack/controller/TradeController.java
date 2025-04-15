package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.persistence.entity.TradeRequestEntity;
import com.marketplace.MarketBack.persistence.entity.TradeStatus;
import com.marketplace.MarketBack.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeController {
    @Autowired
    private TradeService tradeService;

    // Create a product exchange offer
    @PostMapping("/request")
    public ResponseEntity<TradeRequestEntity> requestTrade(
            @RequestParam Long receiverId,
            @RequestParam Long offeredProductId,
            @RequestParam Long requestedProductId,
            Authentication authentication) {
        return ResponseEntity.ok(tradeService.requestTrade(receiverId, requestedProductId, offeredProductId, authentication));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TradeRequestEntity>> getUserTrades(@PathVariable Long userId) {
        return ResponseEntity.ok(tradeService.getUserTrades(userId));
    }

    //Get trade details
    @GetMapping("/trade-details/{tradeId}")
    public ResponseEntity<?> getTradeById(@PathVariable Long tradeId){
        return ResponseEntity.ok(tradeService.getTradeById(tradeId));
    }

    //Get all the offers that a user has made
    @GetMapping("/get-all-offers")
    public ResponseEntity<?> getAllOffersByUser(Authentication authentication){
        return ResponseEntity.ok(tradeService.getTradeByRequesterId(authentication));
    }
    //Get all the request that a user has received
    @GetMapping("/get-all-requests")
    public ResponseEntity<?> getAllRequestsByUser(Authentication authentication){
        return ResponseEntity.ok(tradeService.getTradeByReceiverId(authentication));
    }

    // Allows to accept or reject the request
    @PutMapping("/{tradeId}/status")
    public ResponseEntity<TradeRequestEntity> updateStatus(@PathVariable Long tradeId,
                                                    @RequestParam TradeStatus status) {
        return ResponseEntity.ok(tradeService.updateTradeStatus(tradeId, status));
    }


}
