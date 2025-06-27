package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.UserImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserImageRepository extends JpaRepository<UserImageEntity,Long> {
    List<UserImageEntity> findUserImageEntityByUserId(Long id);
}
