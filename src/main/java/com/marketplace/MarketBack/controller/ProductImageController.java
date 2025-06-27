package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.persistence.entity.ProductImageEntity;
import com.marketplace.MarketBack.service.CloudinaryService;
import com.marketplace.MarketBack.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductImageController {

    @Autowired
    private ProductImageService imageService;

    @Autowired
    private CloudinaryService cloudinaryService;

    //Upload product image
    @PostMapping("/{idProduct}/upload-image")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long idProduct,
            @RequestParam("image") MultipartFile file) throws IOException {

        try {
            Map <String, String> imageUrl = cloudinaryService.uploadFile(file);
            if(!imageUrl.isEmpty()){
                imageService.saveImage(idProduct, imageUrl);
            }
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al subir la imagen: " + e.getMessage());
        }

//        ProductImageEntity image = imageService.uploadImage(idProduct, file);
//        return ResponseEntity.ok(image);
    }

    // Get product image
//    @GetMapping("/{id}/images")
//    public ResponseEntity<List<ProductImageEntity>> listImages(@PathVariable Long id) {
////        return ResponseEntity.ok(imageService.getImages(id));
//    }

//    Delete a product image
//    @DeleteMapping("/{id}/images/{imageId}")
//    public ResponseEntity<Void> deleteImage(@PathVariable Long id, @PathVariable Long imageId) throws IOException {
//        imageService.deleteImage(id, imageId);
//        return ResponseEntity.noContent().build();
//    }


}
