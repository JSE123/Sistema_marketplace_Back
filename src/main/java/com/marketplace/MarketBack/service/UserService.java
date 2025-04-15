package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.controller.dto.ReputationDTO;
import com.marketplace.MarketBack.controller.dto.UserProfileDto;
import com.marketplace.MarketBack.persistence.entity.ReputationEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.repository.ReputationRepository;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReputationRepository reputationRepository;

    private final String uploadDir = "src/main/resources/static/avatars/";


    public List<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }

    public UserEntity GetUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public void updateUserProfile(Long id, UserProfileDto userProfileDto) {
        UserEntity user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        user.setName(userProfileDto.name());
        user.setDescription(userProfileDto.description());
        user.setPhone(userProfileDto.phone());
        userRepository.save(user);
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public boolean updateAvatar(String fileName, Long id){
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserEntity user = optionalUser.get();
            user.setAvatar(fileName);
            userRepository.save(user);
            return true;
        }
        return false;

    }

    public String saveAvatar(MultipartFile file) throws IOException{

        String fileName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();
        Path filepath = Paths.get(uploadDir + fileName);
        Files.createDirectories(filepath.getParent());
        Files.write(filepath, file.getBytes());
        return fileName;
    }

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
}
