package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CateoryRepository extends JpaRepository<CategoryEntity, Long> {
}
