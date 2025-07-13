package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("SELECT m FROM MessageEntity m WHERE " +
            "m.conversation.id = :conversationId AND " +
            "m.recipient.id = :userId AND " +
            "m.timestamp > :lastCheck " +
            "ORDER BY m.timestamp ASC")
    List<MessageEntity> findNewMessages(Long userId, Long conversationId, LocalDateTime lastCheck);
}
