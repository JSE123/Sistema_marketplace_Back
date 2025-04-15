package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.ReputationEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReputationRepository extends JpaRepository<ReputationEntity, Long> {

    List<ReputationEntity> findByRatedUser(UserEntity user);
}
