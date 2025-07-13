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














@Data
@Entity
@Table(name = "tbl_conversations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToMany
//    private Set<UserEntity> participants = new HashSet<>();

    @ManyToOne
    private UserEntity participant1;

    @ManyToOne
    private UserEntity participant2;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY)
    private List<MessageEntity> messages = new ArrayList<>();

    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        lastUpdated = LocalDateTime.now();
    }

}

