package com.marketplace.MarketBack.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Usuario que recibe la notificación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity recipient;

    // Tipo: STOCK_LOW, NEW_MESSAGE, etc.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // Contenido de la notificación
    @Column(nullable = false, length = 500)
    private String content;

    // Fecha de creación
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Estado de lectura
    @Column(nullable = false)
    private boolean read = false;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
