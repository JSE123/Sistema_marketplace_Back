package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntityByUsername(String username);

    boolean existsByUsername(String username);
}
