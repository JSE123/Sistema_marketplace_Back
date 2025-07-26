package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.NotificationEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity,Long> {
    List<NotificationEntity> findByRecipientOrderByCreatedAtDesc(UserEntity user);

    Optional<List<NotificationEntity>> findByRecipientId(long recipientId);
}
