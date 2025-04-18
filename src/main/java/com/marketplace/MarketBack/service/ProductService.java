package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.controller.dto.ProductDTO;
import com.marketplace.MarketBack.controller.dto.ProductUpdateDTO;
import com.marketplace.MarketBack.exception.custom.NotFoundException;
import com.marketplace.MarketBack.exception.enums.ErrorCode;
import com.marketplace.MarketBack.persistence.entity.CategoryEntity;
import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.repository.CateoryRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;


    @Autowired
    CateoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;

    public ProductEntity createProduct(ProductDTO productDTO, Authentication authentication) {

        UserEntity user = userRepository.findUserEntityByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("No se encontro el usuario"));
        CategoryEntity category = categoryRepository.findById(productDTO.categoryId()).orElseThrow(() -> new NotFoundException("Categoria no encontrada", ErrorCode.CATEGORY_NOT_FOUND));
//        CategoryEntity category = categoryRepository.findById(productDTO.categoryId()).orElseThrow(() -> new RuntimeException("no se encontro la categoria"));


        ProductEntity product = ProductEntity.builder()
                .title(productDTO.title())
                .description(productDTO.description())
                .user(user)
                .category(category)
                .status(productDTO.status())
                .location(productDTO.location())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return productRepository.save(product);
    }

    public ProductEntity getProduct(Long id) {
//        return productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("No existe el producto"));
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id,
                        ErrorCode.USER_NOT_FOUND));
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(Long id) {
        ProductEntity producto = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productRepository.delete(producto);
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

    public List<ProductEntity> getProductsByUserId(Authentication authentication) {
        UserEntity user = userRepository.findUserEntityByUsername(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));

        return productRepository.findByUserId(user.getId());
    }


//    public List<ProductEntity> searchProducts(ProductFilterDTO productFilterDTO) {
//        return productRepository.searchProducts(productFilterDTO.title(),productFilterDTO.status(), productFilterDTO.categoryId(), productFilterDTO.location());
//    }
}
