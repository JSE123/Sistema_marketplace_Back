package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.persistence.entity.ProductImageEntity;
import com.marketplace.MarketBack.persistence.repository.ProductImageRepository;
import com.marketplace.MarketBack.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ProductImageService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductImageRepository productImageRepository;

    private final String uploadDir = "C:/Proyecto_marketplace/Backend/uploads";
//    private final String uploadDir = "src/main/resources/uploads/products";

    public ProductImageEntity uploadImage(Long productId, MultipartFile file) throws IOException {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        String directory = uploadDir + "/" + productId;
        Files.createDirectories(Paths.get(directory));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(directory, fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/products/")
                .path(productId + "/")
                .path(fileName)
                .toUriString();

        ProductImageEntity image = ProductImageEntity.builder()
                .fileName(fileName)
                .filePath(path.toString())
                .url(imageUrl)
                .product(product)
                .build();
        return productImageRepository.save(image);
    }

    public List<ProductImageEntity> getImages(Long productId) {
        return productImageRepository.findByProductId(productId);
    }

    public void deleteImage(Long productId, Long imageId) throws IOException {
        ProductImageEntity image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        if (!image.getProduct().getId().equals(productId)) {
            throw new RuntimeException("La imagen no pertenece a este producto");
        }

        Path path = Paths.get(image.getFilePath());
        Files.deleteIfExists(path);
        productImageRepository.delete(image);
    }
}
