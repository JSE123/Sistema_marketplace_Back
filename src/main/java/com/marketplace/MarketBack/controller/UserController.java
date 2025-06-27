package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.*;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.entity.UserImageEntity;
import com.marketplace.MarketBack.service.CloudinaryService;
import com.marketplace.MarketBack.service.UserImageService;
import com.marketplace.MarketBack.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserImageService userImageService;


    //Get all users
    @GetMapping
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //Obtener perfil de usuario
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){

        return ResponseEntity.ok(userService.getUserById(id));
    }

    //Update user profile
    @PutMapping()
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody UserProfileDto userProfileDto){
        userService.updateUserProfile(authentication, userProfileDto);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{idUser}/avatar")
    public ResponseEntity<?> saveAvatar(@PathVariable Long idUser,
                                        @RequestParam("image") MultipartFile file, Authentication authentication) throws IOException{
        try{
          Map<String, String> image = cloudinaryService.uploadFile(file, "UserImages");

          if(!image.isEmpty()){
              //guardar registro en tabla
              UserImageDTOResponse userImage = userImageService.saveImage(authentication, image, "avatar");
              return ResponseEntity.status(HttpStatus.OK).body(userImage);

          }else{
              return ResponseEntity.ok("Ocurrio un error al guardar el avatar");

          }
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error al subir el avatar: " + e.getMessage());
        }
    }

    @PostMapping("/{idUser}/portada")
    public ResponseEntity<?> savePortada(@PathVariable Long idUser,
                                        @RequestParam("image") MultipartFile file, Authentication authentication) throws IOException{
        try{
          Map<String, String> image = cloudinaryService.uploadFile(file, "UserImages");

          if(!image.isEmpty()){
              //guardar registro en tabla
              UserImageDTOResponse userImage = userImageService.saveImage(authentication, image, "portada");
//              return ResponseEntity.ok("Avatar guardado correctamente"+userImage);
              return ResponseEntity.status(HttpStatus.CREATED).body(userImage);
          }else{
              return ResponseEntity.ok("Ocurrio un error al guardar la portada");

          }
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error al subir la portada: " + e.getMessage());
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

    //delete user
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("Eliminano correctamente");
    }

    //Update role
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable long id,@RequestBody UpdateRolesRequestDTO updateRolesRequestDTO){
        return ResponseEntity.ok(userService.updateRoles(id, updateRolesRequestDTO));
    }
}
