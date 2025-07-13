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

import java.util.List;

@Service
public class SaleService {

    @Autowired
    private SalesRepository saleRepository;

    //inject user repository to verify if the user exists
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

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

        // if sale saved is successful, decrease the product stock
        productService.decreaseStock(product.getId(), saleDto.amount());


        return new SaleDto(
                saleSaved.getProduct().getId(),
                saleSaved.getAmount(),
                saleSaved.getTotal(),
                saleSaved.getUser().getId()
        );
    }


    public List<SaleDto> getSaleByUserId(long userId) {
        // Check if the user exists
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Retrieve sales by user ID
        List<SaleEntity> sales = saleRepository.findByUser(user);

        // If no sales found, throw an exception
        if (sales.isEmpty()) {
            throw new NotFoundException("No sales found for this user", ErrorCode.SALE_NOT_FOUND);
        }

        // Map SaleEntity to SaleDto if needed, or return directly
        List<SaleDto> saleDto = sales.stream()
                .map(sale -> new SaleDto(
                        sale.getProduct().getId(),
                        sale.getAmount(),
                        sale.getTotal(),
                        sale.getUser().getId()))
                .toList();

        return saleDto;
    }
}

