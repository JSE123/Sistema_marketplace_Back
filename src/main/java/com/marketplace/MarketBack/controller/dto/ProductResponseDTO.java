package com.marketplace.MarketBack.controller.dto;

import com.marketplace.MarketBack.persistence.entity.EstadoProducto;
import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@Builder
@Data
public class ProductResponseDTO {

    private Long id;
    private String title;
    private String description;
    private long price;
    private List<String> imageUrls;
    private EstadoProducto status = EstadoProducto.NUEVO;
    private UserEntity user = null;
    private LocalDateTime updatedAt ;


//    public ProductDTO(ProductEntity product) {
//        this.id = product.getId();
//        this.name = product.getName();
//        this.description = product.getDescription();
//        this.price = product.getPrice();
//        this.imageUrls = product.getImages().stream()
//                .map(ProductImage::getImageUrl)
//                .collect(Collectors.toList());
//    }
}
