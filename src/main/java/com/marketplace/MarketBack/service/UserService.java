package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.controller.dto.*;
import com.marketplace.MarketBack.exception.custom.NotFoundException;
import com.marketplace.MarketBack.exception.enums.ErrorCode;
import com.marketplace.MarketBack.persistence.entity.ReputationEntity;
import com.marketplace.MarketBack.persistence.entity.RoleEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.entity.UserImageEntity;
import com.marketplace.MarketBack.persistence.repository.ReputationRepository;
import com.marketplace.MarketBack.persistence.repository.RoleRepository;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReputationRepository reputationRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final String uploadDir = "src/main/resources/static/avatars/";


    public List<UserDto> getAllUsers(){
        List<UserEntity> users =  userRepository.findAll();

        return users
                .stream()
                .map(UserDto::fromEntity)
                .toList();

    }



    public UserEntity GetUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public void updateUserProfile(Authentication authentication, UserProfileDto userProfileDto) {
        UserEntity user = userRepository.findUserEntityByUsername(authentication.getName())
                        .orElseThrow(() -> new NotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));



        user.setName(userProfileDto.name());
        user.setLastName(userProfileDto.lastName());
        user.setEmail(userProfileDto.email());
        user.setDescription(userProfileDto.description());
        user.setPhone(userProfileDto.phone());
        user.setAddress(userProfileDto.address());

        userRepository.save(user);
    }

    public ProfileResponseDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String avatarUrl = "", portadaUrl = "";

        for (UserImageEntity image : user.getImage()) {
            if ("avatar".equalsIgnoreCase(image.getType())) {
                avatarUrl = image.getUrl();
            } else if ("portada".equalsIgnoreCase(image.getType())) {
                portadaUrl = image.getUrl();
            }

            // Si ya tenemos ambas imágenes, podemos terminar el bucle
            if (!avatarUrl.isEmpty() && !portadaUrl.isEmpty()) {
                break;
            }
        }

        return new ProfileResponseDTO(
                user.getUsername(),
                user.getName(),
                user.getLastName(),
                user.getAddress(),
                user.getPhone(),
                user.getEmail(),
                user.getDescription(),
                avatarUrl,
                portadaUrl,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }



//    public String saveAvatar(MultipartFile file) throws IOException{
//
//        String fileName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();
//        Path filepath = Paths.get(uploadDir + fileName);
//        Files.createDirectories(filepath.getParent());
//        Files.write(filepath, file.getBytes());
//        return fileName;
//    }

    public byte[] getImage(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir + fileName);
        return Files.readAllBytes(filePath);
    }

    public void rateUser(Long id, ReputationDTO request) {
        UserEntity ratedUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ReputationEntity rating = new ReputationEntity();
        rating.setRatedUser(ratedUser);
        rating.setRaterUser(userRepository.findById(request.userId()).orElseThrow());
        rating.setRating(request.rating());
        rating.setComment(request.comment());

        reputationRepository.save(rating);
    }

    public Double getReputation(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<ReputationEntity> ratings = reputationRepository.findByRatedUser(user);
        return ratings.stream().mapToInt(ReputationEntity::getRating).average().orElse(0);


    }

    public void deleteUser(long id){
        userRepository.deleteById(id);
    }

    public UserDto updateRoles(long id, UpdateRolesRequestDTO updateRolesRequestDTO){
        Set<RoleEntity> roles = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnumIn(updateRolesRequestDTO.roleListName()));

        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found", ErrorCode.USER_NOT_FOUND));



        if(roles.isEmpty()){
            throw new IllegalArgumentException("The roles specified does not exist.");
        }

        user.setRoles(roles);
        UserEntity userUpdated = userRepository.save(user);

        return new UserDto(userUpdated.getId(),userUpdated.getUsername(), userUpdated.getName(), userUpdated.getLastName(), userUpdated.getEmail(),userUpdated.getDescription(), userUpdated.getPhone(), userUpdated.getAddress(), userUpdated.isEnable(), userUpdated.getCreatedAt(), userUpdated.getUpdatedAt(), userUpdated.getRoles().stream().map(role -> role.getRoleEnum().name()).collect(Collectors.toSet()));
    }
}
