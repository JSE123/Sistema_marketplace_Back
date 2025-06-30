package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.ProductDTO;
import com.marketplace.MarketBack.controller.dto.ProductFilterDTO;
import com.marketplace.MarketBack.controller.dto.ProductResponseDTO;
import com.marketplace.MarketBack.controller.dto.ProductUpdateDTO;
import com.marketplace.MarketBack.persistence.entity.EstadoProducto;
import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.service.ProductService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products/")
public class ProductController {

    @Autowired
    ProductService productService;

    //Create a new product
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO, Authentication authentication){

        return ResponseEntity.ok(productService.createProduct(productDTO, authentication));
    }

    //Get product by product id
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProduct(id));
    }

    //Get all products
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    //Get product by owner
    @GetMapping("get-my-products")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByUserId(Authentication authentication){
        return ResponseEntity.ok(productService.getProductsByUserId(authentication));
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<ProductEntity>> searchProducts( @RequestParam(required = false) String title,
//                                                               @RequestParam(required = false) EstadoProducto status,
//                                                               @RequestParam(required = false) Long categoryId,
//                                                               @RequestParam(required = false) String location) {
//
//
//        ProductFilterDTO productFilterDTO = new ProductFilterDTO(title, status, categoryId, location);
//        return ResponseEntity.ok(productService.searchProducts(productFilterDTO));
//    }

    //Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id, Authentication authentication){
        productService.deleteProduct(id, authentication);
        return ResponseEntity.noContent().build();
//        return ResponseEntity.ok("Eliminado correctamente");
    }

    //Update product
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO productUpdateDTO, Authentication authentication){
        String username = authentication.getName();
        productService.updateProduct(id, productUpdateDTO, username);
        return ResponseEntity.noContent().build();
    }

    //Get product by category
    @GetMapping("category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductByCategory(@PathVariable long categoryId){
        return ResponseEntity.ok(productService.getProductByCategoryId(categoryId));
    }



}
