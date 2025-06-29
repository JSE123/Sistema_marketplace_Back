package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.controller.dto.SaleDto;
import com.marketplace.MarketBack.exception.custom.NotFoundException;
import com.marketplace.MarketBack.exception.enums.ErrorCode;
import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.persistence.entity.SaleEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.repository.ProductRepository;
import com.marketplace.MarketBack.persistence.repository.SalesRepository;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SaleService {

    @Autowired
    private SalesRepository saleRepository;

    //inject user repository to verify if the user exists
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    //Save new sales
    public SaleDto createSale(SaleDto saleDto){
        //verify if the product exists and is available
        ProductEntity product = productRepository.findById(saleDto.productId()).orElseThrow(() -> new NotFoundException("Product not found", ErrorCode.PRODUCT_NOT_FOUND));

        //verify if the user exists
        UserEntity user = userRepository.findById(saleDto.userId()).orElseThrow(() -> new NotFoundException("User not found", ErrorCode.USER_NOT_FOUND));

        // Create a new SaleEntity from the SaleDto
        SaleEntity sale = SaleEntity.builder()
                .amount(saleDto.amount())
                .total(saleDto.total())
                .user(user)
                .product(product)
                .build();

        // Save the sale entity to the database
        SaleEntity saleSaved = saleRepository .save(sale);
        // Map the Saleentity to the SaleDto

        return new SaleDto(
                saleSaved.getProduct().getId(),
                saleSaved.getAmount(),
                saleSaved.getTotal(),
                saleSaved.getUser().getId()
        );
    }


}
