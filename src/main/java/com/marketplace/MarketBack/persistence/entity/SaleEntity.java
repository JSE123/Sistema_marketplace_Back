package com.marketplace.MarketBack.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sales_tbl")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
