package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.persistence.entity.ChatMessageEntity;
import com.marketplace.MarketBack.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trades/{tradeId}/chat")
public class ChatMessageController {
    @Autowired
    private ChatMessageService chatMessageService;

    //Function for send message
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @PathVariable Long tradeId,
            @RequestParam String message,
            Authentication authentication
    ) {

        return ResponseEntity.ok(chatMessageService.sendMessage(tradeId, message, authentication));
    }


    // function that allows to get the messages
    @GetMapping
    public ResponseEntity<List<?>> getMessages(@PathVariable Long tradeId) {
        return ResponseEntity.ok(chatMessageService.getChatMessages(tradeId));
    }
}
