package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.EstadoProducto;
import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByIdAndUser(Long id, UserEntity user);

//    @Query("SELECT p FROM ProductEntity p WHERE " +
//            "(:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
//            "(:status IS NULL OR p.status = :status) AND " +
//            "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
//            "(:location IS NULL OR LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%')))")
//    List<ProductEntity> searchProducts(@Param("title") String title,
//                                       @Param("status") EstadoProducto status,
//                                       @Param("categoryId") Long categoryId,
//                                       @Param("location") String location);
}
