package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.TradeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<TradeRequestEntity, Long> {
    List<TradeRequestEntity> findByRequesterIdOrReceiverId(Long userId, Long userId1);

    List<TradeRequestEntity> findByReceiverId(Long id);

    List<TradeRequestEntity> findByRequesterId(Long id);
}
