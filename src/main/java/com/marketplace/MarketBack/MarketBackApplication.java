package com.marketplace.MarketBack;

import com.marketplace.MarketBack.persistence.entity.PermissionEntity;
import com.marketplace.MarketBack.persistence.entity.RoleEntity;
import com.marketplace.MarketBack.persistence.entity.RoleEnum;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class MarketBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketBackApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository){
		return args -> {

			//crear permisos
			PermissionEntity createPermission = PermissionEntity.builder()
					.name("CREATE")
					.build();
			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();
			PermissionEntity updatePermission = PermissionEntity.builder()
					.name("UPDATE")
					.build();


			// crear roles
			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(createPermission, readPermission, updatePermission))
					.build();
			RoleEntity roleUser = RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permissionList(Set.of(createPermission, readPermission))
					.build();


			// crear usuarios
			UserEntity userJose = UserEntity.builder()
					.username("jose")
					.password("$2a$12$sdxq8qrhdga0BoVYxUzRFeq4mk0RTg3qqobOTigb6Rx6CmgC9g7G6")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.build();
			UserEntity userUsuario1 = UserEntity.builder()
					.username("usuario1")
					.password("$2a$10$cMY29RPYoIHMJSuwRfoD3eQxU1J5Rww4VnNOUOAEPqCBshkNfrEf6")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleUser))
					.build();


			userRepository.saveAll(List.of(userJose, userUsuario1));
		};
	}

}
