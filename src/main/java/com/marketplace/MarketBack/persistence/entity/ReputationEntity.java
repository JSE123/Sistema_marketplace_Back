package com.marketplace.MarketBack.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reputations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReputationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rated_user_id")
    private UserEntity ratedUser;

    @ManyToOne
    @JoinColumn(name = "rater_user_id")
    private UserEntity raterUser;

    private int rating;
    private String comment;

}
