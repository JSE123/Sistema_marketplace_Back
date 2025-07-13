package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.SaleEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findByUser(UserEntity user);
}
