package com.marketplace.MarketBack.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_traders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que solicita el intercambio
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private UserEntity requester;

    // Usuario que recibe la solicitud
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserEntity receiver;

    // Producto que ofrece el solicitante
    @ManyToOne
    @JoinColumn(name = "offered_product_id", nullable = false)
    private ProductEntity offeredProduct;

    // Producto deseado del receptor
    @ManyToOne
    @JoinColumn(name = "requested_product_id", nullable = false)
    private ProductEntity requestedProduct;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = TradeStatus.PENDING;
    }

}
