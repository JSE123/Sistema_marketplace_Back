package com.marketplace.MarketBack.service;

import ch.qos.logback.core.status.ErrorStatus;
import com.marketplace.MarketBack.controller.dto.ChatMessageDTO;
import com.marketplace.MarketBack.exception.custom.NotFoundException;
import com.marketplace.MarketBack.exception.enums.ErrorCode;
import com.marketplace.MarketBack.persistence.entity.ChatMessageEntity;
import com.marketplace.MarketBack.persistence.entity.TradeRequestEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.repository.ChatMessageRepository;
import com.marketplace.MarketBack.persistence.repository.TradeRepository;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private UserRepository userRepository;

    public ChatMessageEntity sendMessage(Long tradeId, String messageContent, Authentication authentication) {
        UserEntity sender = userRepository.findUserEntityByUsername(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));

        TradeRequestEntity trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new NotFoundException("Trade no encontrado", ErrorCode.TRADE_NOT_FOUND));

        ChatMessageEntity message = ChatMessageEntity.builder()
                .trade(trade)
                .sender(sender)
                .message(messageContent)
                .build();
        return chatMessageRepository.save(message);
    }

    public List<ChatMessageDTO> getChatMessages(Long tradeId) {
        return chatMessageRepository.findByTradeIdOrderByTimestampAsc(tradeId).stream()
                .map(msg -> new ChatMessageDTO(
                        msg.getId(),
                        msg.getMessage(),
                        msg.getSender().getUsername(),
                        msg.getTimestamp()
                )).toList();
    }
}
