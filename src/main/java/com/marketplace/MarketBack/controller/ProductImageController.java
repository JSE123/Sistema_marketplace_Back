package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.persistence.entity.ProductImageEntity;
import com.marketplace.MarketBack.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductImageController {
    @Autowired
    private ProductImageService imageService;

    //Upload product image
    @PostMapping("/{idProduct}/upload-image")
    public ResponseEntity<ProductImageEntity> uploadImage(
            @PathVariable Long idProduct,
            @RequestParam("image") MultipartFile file) throws IOException {

        ProductImageEntity image = imageService.uploadImage(idProduct, file);
        return ResponseEntity.ok(image);
    }

    // Get product image
    @GetMapping("/{id}/images")
    public ResponseEntity<List<ProductImageEntity>> listImages(@PathVariable Long id) {
        return ResponseEntity.ok(imageService.getImages(id));
    }

    //Delete a product image
    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id, @PathVariable Long imageId) throws IOException {
        imageService.deleteImage(id, imageId);
        return ResponseEntity.noContent().build();
    }


}
