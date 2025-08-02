package com.marketplace.MarketBack;

import com.marketplace.MarketBack.persistence.entity.PermissionEntity;
import com.marketplace.MarketBack.persistence.entity.RoleEntity;
import com.marketplace.MarketBack.persistence.entity.RoleEnum;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.repository.PermissionRepository;
import com.marketplace.MarketBack.persistence.repository.RoleRepository;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PermissionRepository permissionRepository,
                           RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Crear permisos si no existen
        List<String> permissions = List.of("READ", "WRITE", "DELETE");
        for (String name : permissions) {
            permissionRepository.findByName(name)
                    .orElseGet(() -> permissionRepository.save(new PermissionEntity(name)));
        }

        // Obtener permisos creados
        Set<PermissionEntity> allPermissions = new HashSet<>(permissionRepository.findAll());

        // Crear rol ADMIN si no existe
        RoleEntity adminRole = roleRepository.findByRoleEnum(RoleEnum.ADMIN)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity();
                    newRole.setRoleEnum(RoleEnum.ADMIN);
                    newRole.setPermissionList(allPermissions);
                    return roleRepository.save(newRole);
                });
        RoleEntity userRole = roleRepository.findByRoleEnum(RoleEnum.USER)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity();
                    newRole.setRoleEnum(RoleEnum.USER);
                    newRole.setPermissionList(allPermissions);
                    return roleRepository.save(newRole);
                });
        RoleEntity sellerRole = roleRepository.findByRoleEnum(RoleEnum.SELLER)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity();
                    newRole.setRoleEnum(RoleEnum.SELLER);
                    newRole.setPermissionList(allPermissions);
                    return roleRepository.save(newRole);
                });

        // Crear usuario admin si no existe
        if (userRepository.findUserEntityByUsername("admin").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
            System.out.println("✅ Usuario admin creado.");
        }
    }
}
