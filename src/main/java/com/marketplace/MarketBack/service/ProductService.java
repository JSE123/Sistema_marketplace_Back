package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.controller.dto.ProductDTO;
import com.marketplace.MarketBack.controller.dto.ProductResponseDTO;
import com.marketplace.MarketBack.controller.dto.ProductUpdateDTO;
import com.marketplace.MarketBack.exception.custom.NotFoundException;
import com.marketplace.MarketBack.exception.enums.ErrorCode;
import com.marketplace.MarketBack.persistence.entity.CategoryEntity;
import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.persistence.entity.ProductImageEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.repository.CateoryRepository;
import com.marketplace.MarketBack.persistence.repository.ProductImageRepository;
import com.marketplace.MarketBack.persistence.repository.ProductRepository;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;


    @Autowired
    CateoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ProductImageRepository imageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public ProductDTO createProduct(ProductDTO productDTO, Authentication authentication) {

        UserEntity user = userRepository.findUserEntityByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("No se encontro el usuario"));
        CategoryEntity category = categoryRepository.findById(productDTO.categoryId()).orElseThrow(() -> new NotFoundException("Categoria no encontrada", ErrorCode.CATEGORY_NOT_FOUND));
//        CategoryEntity category = categoryRepository.findById(productDTO.categoryId()).orElseThrow(() -> new RuntimeException("no se encontro la categoria"));


        ProductEntity product = ProductEntity.builder()
                .title(productDTO.title())
                .description(productDTO.description())
                .user(user)
                .price(productDTO.price())
                .stock(productDTO.stock())
                .category(category)
                .status(productDTO.status())
                .location(productDTO.location())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

//        return productRepository.save(product);
        ProductEntity newProduct = productRepository.save(product);
        return new ProductDTO(newProduct.getTitle(),newProduct.getDescription(), newProduct.getPrice(), newProduct.getStock(), newProduct.getStatus(), newProduct.getCategory().getId(), newProduct.getLocation(), newProduct.getId() );
    }

    public ProductResponseDTO getProduct(Long id) {
//        return productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("No existe el producto"));
        ProductEntity product =  productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id,
                        ErrorCode.USER_NOT_FOUND));

        return ProductResponseDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .imageUrls(product.getImages()
                        .stream()
                        .map(ProductImageEntity::getUrl)
                        .collect(Collectors.toList()))
                .description(product.getDescription())
                .status(product.getStatus())
                .price(product.getPrice())
                .stock(product.getStock())
                .user(product.getUser().getId().toString())
                .category(product.getCategory())
                .build();
//        return new ProductResponseDTO(
//                product.getId(),
//                product.getTitle(),
//                product.getDescription(),
//                product.getImages()
//                        .stream()
//                        .map(ProductImageEntity::getUrl)
//                        .collect(Collectors.toList()));
    }


    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
//        return productRepository.findAll().stream()
//                .map(product -> new ProductResponseDTO(
//                                product.getId(),
//                                product.getTitle(),
//                                product.getDescription(),
//                                product.getImages().stream()
//                                        .map(ProductImageEntity::getUrl)
//                                        .collect(Collectors.toList())
//                ))
//                .collect(Collectors.toList());
        return productRepository.findAll().stream()
                .map(product -> {
                    return ProductResponseDTO.builder()
                            .id(product.getId())
                            .title(product.getTitle())
                            .price(product.getPrice())
                            .imageUrls(product.getImages().stream()
                                    .map(ProductImageEntity::getUrl)
                                    .collect(Collectors.toList()))
                            .build();
                }).collect(Collectors.toList());
    }

    public void deleteProduct(Long id, Authentication auth) {
        ProductEntity product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        //verify if the product belongs to the user in session
        UserEntity user = userRepository.findUserEntityByUsername(auth.getName()).orElseThrow(() -> new NotFoundException("User not found", ErrorCode.USER_NOT_FOUND));
        if(!product.getUser().equals(user)){
            return;
        }

        cloudinaryService.deleteImageFromCloudinary(product.getImages());

        productRepository.delete(product);
    }

    @Transactional
    public void updateProduct(Long id, ProductUpdateDTO productUpdateDTO, String username) {
        UserEntity user = userRepository.findUserEntityByUsername(username).orElseThrow(() -> new RuntimeException("No se encontro el usuario"));

        ProductEntity product = productRepository.findByIdAndUser(id,user).orElseThrow(() -> new RuntimeException("No se encontro el producto"));
//        ProductEntity product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        BeanUtils.copyProperties(productUpdateDTO, product, getNullPropertyName(productUpdateDTO));

        productRepository.save(product);
    }

    private String[] getNullPropertyName(Object source){
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds){
            Object srcValue = src.getPropertyValue(pd.getName());
            if(srcValue == null) emptyNames.add(pd.getName());

        }
        return emptyNames.toArray(new String[0]);
    }

    public List<ProductResponseDTO> getProductsByUserId(Authentication authentication) {
        UserEntity user = userRepository.findUserEntityByUsername(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));

        return productRepository.findByUserId(user.getId()).stream().map(productEntity -> {
            return ProductResponseDTO.builder()
                    .description(productEntity.getDescription())
                    .id(productEntity.getId())
                    .title(productEntity.getTitle())
                    .status(productEntity.getStatus())
                    .price(productEntity.getPrice())
                    .stock(productEntity.getStock())
                    .updatedAt(productEntity.getUpdatedAt())
                    .imageUrls(productEntity.getImages().stream().map(ProductImageEntity::getUrl)
                            .collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductByCategoryId(long categoryId) {
        //Verify if the category exists
        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Categoria no encontrada", ErrorCode.CATEGORY_NOT_FOUND));
        List<ProductEntity> products = productRepository.findByCategory(category);

        List<ProductResponseDTO> productList = new ArrayList<>();

        products.stream().map( product ->
                productList.add(ProductResponseDTO.builder()
                                .id(product.getId())
                                .title(product.getTitle())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .imageUrls(product.getImages().stream().map(ProductImageEntity::getUrl).collect(Collectors.toList()))
                                .status(product.getStatus())
                                .updatedAt(product.getUpdatedAt())
                        .build())

        ).collect(Collectors.toList());

        return productList;

    }


//    public List<ProductEntity> searchProducts(ProductFilterDTO productFilterDTO) {
//        return productRepository.searchProducts(productFilterDTO.title(),productFilterDTO.status(), productFilterDTO.categoryId(), productFilterDTO.location());
//    }

}
