package com.marketplace.MarketBack.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "sales_tbl")
@Data
@Builder
public class SaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int amount;
    private long total;

    @ManyToOne(fetch = FetchType.EAGER)
    ProductEntity product;

    @ManyToOne(fetch = FetchType.EAGER)
    UserEntity user;

}
