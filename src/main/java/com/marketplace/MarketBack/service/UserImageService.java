package com.marketplace.MarketBack.service;

import com.cloudinary.Cloudinary;
import com.marketplace.MarketBack.controller.dto.UserImageDTOResponse;
import com.marketplace.MarketBack.exception.custom.NotFoundException;
import com.marketplace.MarketBack.exception.enums.ErrorCode;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.entity.UserImageEntity;
import com.marketplace.MarketBack.persistence.repository.UserImageRepository;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserImageService {
    @Autowired
    private UserImageRepository userImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    //Save image
    public UserImageDTOResponse saveImage(Authentication auth, Map<String, String> image, String type) throws IOException {


        UserEntity user = userRepository.findUserEntityByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));

        //verificar si hay imagen
        List<UserImageEntity> uImage = userImageRepository.findUserImageEntityByUserId(user.getId());

        //verificacion si existe el tipo de la imagen
        Optional<UserImageEntity> userImage = uImage.stream()
                .filter(
                        userI -> userI.getType().equals(type)
                ).findFirst();

        if(userImage.isPresent()){
            //eliminar el registro y la imagen de cloudinary
            deleteImageUser(userImage.get().getId());

            System.out.println(userImage.get().getImagePublicId());
//            delete image from cloudinary
            cloudinaryService.deleteFile(userImage.get().getImagePublicId());

        }

        UserImageEntity userImageNew = userImageRepository.save(
                UserImageEntity.builder()
                .imagePublicId(image.get("public_id"))
                .user(user)
                .type(type)
                .url(image.get("url"))
                .build()
        );
        return new UserImageDTOResponse(userImageNew.getUrl(), userImageNew.getUser().getUsername(), userImageNew.getType());
    }

    public void deleteImageUser(Long id){
        userImageRepository.deleteById(id);
    }

}
