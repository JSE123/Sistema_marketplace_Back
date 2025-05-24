package com.marketplace.MarketBack.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Tbl_products")
@Builder
public class ProductEntity {
    //id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //title
    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private long price = 0;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int stock = 0;

    //description
    @Column(columnDefinition = "TEXT")
    private String description;

    //user_id FK
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    //status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProducto status;

    //category_id FK
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    //location
    private String location;

    //created_at
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    //updated_at
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<TagEntity> tags = new HashSet<>();


    //para imagens
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImageEntity> images = new ArrayList<>();
}

