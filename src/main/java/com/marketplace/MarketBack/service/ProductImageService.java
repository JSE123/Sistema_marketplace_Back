package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.persistence.entity.ProductImageEntity;
import com.marketplace.MarketBack.persistence.repository.ProductImageRepository;
import com.marketplace.MarketBack.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ProductImageService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductImageRepository productImageRepository;

    private final String uploadDir = "C:/Proyecto_marketplace/Backend/uploads";
//    private final String uploadDir = "src/main/resources/uploads/products";

    public ProductImageEntity saveImage(Long productId, Map<String, String> imageFile) throws IOException {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));



        ProductImageEntity image = ProductImageEntity.builder()
                .url(imageFile.get("url"))
                .publicId(imageFile.get("public_id"))
                .product(product)
                .build();
        return productImageRepository.save(image);
    }

    public List<ProductImageEntity> getImages(Long productId) {
        return productImageRepository.findByProductId(productId);
    }

//    public void deleteImage(Long productId, Long imageId) throws IOException {
//        List<ProductImageEntity> productImages = productImageRepository.findByProductId(productId);
//
////        ProductImageEntity image = productImageRepository.findById(imageId)
////                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
//
//       productImageRepository.deleteById();
//
//        Path path = Paths.get(image.getFilePath());
//        Files.deleteIfExists(path);
//        productImageRepository.delete(image);
//    }
}
