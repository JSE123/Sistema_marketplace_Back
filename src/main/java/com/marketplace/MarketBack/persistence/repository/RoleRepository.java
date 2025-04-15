package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);

    Set<RoleEntity> findByRoleEnumIn(Set<String> roleEnums);
}
