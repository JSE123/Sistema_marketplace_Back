package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepository extends JpaRepository<SaleEntity, Long> {
}
