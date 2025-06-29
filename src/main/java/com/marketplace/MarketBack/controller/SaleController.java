package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.SaleDto;
import com.marketplace.MarketBack.persistence.entity.SaleEntity;
import com.marketplace.MarketBack.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;

    // methods for create a new sale
    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody SaleDto saleEntity) {
        ;
        return ResponseEntity.ok(saleService.createSale(saleEntity));
    }
}
