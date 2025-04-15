package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
