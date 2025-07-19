package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.persistence.entity.ReputationEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReputationRepository extends JpaRepository<ReputationEntity, Long> {

    List<ReputationEntity> findByRatedUser(UserEntity user);

    boolean existsByRaterUserAndProduct(UserEntity raterUser, ProductEntity product);

    ReputationEntity findByRaterUserAndProduct(UserEntity raterUser, ProductEntity product);

    List<ReputationEntity> findByProductId(Long productId);

    @Query("SELECT AVG(r.rating) FROM ReputationEntity r WHERE r.product.id = :productId")
    Optional<Double> findAverageRatingByProductId(@Param("productId") Long productId);
}
