package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.ConversationResponseDto;
import com.marketplace.MarketBack.controller.dto.MessageRequestDto;
import com.marketplace.MarketBack.persistence.entity.MessageEntity;
import com.marketplace.MarketBack.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<?> sendMessage(
            @RequestBody MessageRequestDto request) {

        MessageEntity message = messageService.sendMessage(
                request.getSenderId(),
                request.getRecipientId(),
                request.getContent());


        Map<String, Object> response = new HashMap<>();
        response.put("message", "Enviado");
        response.put("id", message.getId());

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/conversation/{otherUserId}/{userId}")
//    public ResponseEntity<?> getConversation(
//            @PathVariable Long otherUserId,
//            @PathVariable Long userId) {
//
////        Long userId = userDetails.get("id");
//        List<MessageResponseDto> messages = messageService.getConversationMessages(userId, otherUserId);
//
//        return ResponseEntity.ok(messages);
//    }
//

//
    @GetMapping("/updates")
    public ResponseEntity<List<MessageEntity>> checkForUpdates(
            @RequestParam Long conversationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCheck,
            @RequestBody Map<String, Long> userDetails) {

        Long userId = userDetails.get("id");
        List<MessageEntity> newMessages = messageService.getNewMessages(userId, conversationId, lastCheck);
        return ResponseEntity.ok(newMessages);
    }
    @PatchMapping("/mark-as-read/{conversationId}")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long conversationId, Authentication auth) {

        messageService.markMessagesAsRead(conversationId, auth);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<?> getConversations(
            @PathVariable long userId) {


        List<ConversationResponseDto> conversations = messageService.getConversations(userId);
        return ResponseEntity.ok(conversations);
    }
}
