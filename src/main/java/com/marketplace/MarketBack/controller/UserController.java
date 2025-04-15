package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.ReputationDTO;
import com.marketplace.MarketBack.controller.dto.UserProfileDto;
import com.marketplace.MarketBack.persistence.entity.ReputationEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService userService;

    private final String UploadDir = "src/main/resources/static/avatars/";

    //Get all users
    @GetMapping
    public List<UserEntity> getUsers(){
        return userService.getAllUsers();
    }


    //Obtener perfil de usuario
    @GetMapping("/{id}")
    public UserEntity getUser(@PathVariable Long id){
        return userService.getUserById(id);
    }

    //Update user profile
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto){
        userService.updateUserProfile(id, userProfileDto);
        return ResponseEntity.ok("Perfil actualizado correctamente");
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<String> updateAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        try{
            String avatarFileName = userService.saveAvatar(file);
            boolean update = userService.updateAvatar(avatarFileName, id);
            if(update){
                return ResponseEntity.ok("Avatar acutalizado correctamente");
            }else{
                return ResponseEntity.badRequest().body("Error al guardar el avatar");
            }
        }catch (IOException e){
            return ResponseEntity.internalServerError().body("Error al guardar el avatar");
        }

    }


    @GetMapping("/{id}/avatar")
    public ResponseEntity<Resource> getAvatar(@PathVariable Long id){
        try{
            UserEntity user = userService.getUserById(id);
            if(user == null || user.getAvatar() == null){
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(UploadDir + user.getAvatar());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + user.getAvatar() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        }catch (MalformedURLException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    //Rate user
    @PostMapping("/{idRatedUser}/reputation")
    public ResponseEntity<String> rateUser(@PathVariable Long idRatedUser, @RequestBody ReputationDTO reputationDTO){
        userService.rateUser(idRatedUser, reputationDTO);
        return ResponseEntity.ok("Calificacion guardada correctamente");
    }

    //Get user reputation
    @GetMapping("/{idRatedUser}/reputation")
    public ResponseEntity<Double> getReputationById(@PathVariable Long idRatedUser){
        return ResponseEntity.ok(userService.getReputation(idRatedUser));
    }
}
