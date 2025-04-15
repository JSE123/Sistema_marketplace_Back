package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByTradeIdOrderByTimestampAsc(Long tradeId);
}
