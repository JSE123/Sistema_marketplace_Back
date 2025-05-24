package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.UserImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<Long, UserImageEntity> {
}
