package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.ProductDTO;
import com.marketplace.MarketBack.controller.dto.ProductFilterDTO;
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

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products/")
public class ProductController {

    @Autowired
    ProductService productService;

    //Create a new product
    @PostMapping
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductDTO productDTO, Authentication authentication){
        ProductEntity newProduct = productService.createProduct(productDTO, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    //Get product by product id
    @GetMapping("/{id}")
    public ProductEntity getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }

    //Get all products
    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
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
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("eliminado correctamente");
    }

    //Update product
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO productUpdateDTO, Authentication authentication){
        String username = authentication.getName();
        productService.updateProduct(id, productUpdateDTO, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("prueba")
    public Authentication getUserAuthenticated(Authentication authentication){
        return authentication;
    }

}
