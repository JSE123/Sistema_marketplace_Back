package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.SaleDto;
import com.marketplace.MarketBack.persistence.entity.SaleEntity;
import com.marketplace.MarketBack.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sales/")
public class SaleController {
    @Autowired
    private SaleService saleService;

    // methods for create a new sale
    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody SaleDto saleEntity) {

        return ResponseEntity.ok(saleService.createSale(saleEntity));
    }

    // method for get a sale by userid
    @GetMapping("{userId}/get-by-user")
    public ResponseEntity<?> getSaleByUserId(@PathVariable long userId) {
        List<SaleDto> sale = saleService.getSaleByUserId(userId);
        if (sale != null) {
            return ResponseEntity.ok(sale);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
