package com.marketplace.MarketBack.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.marketplace.MarketBack.persistence.entity.ProductImageEntity;
import com.marketplace.MarketBack.persistence.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ProductImageRepository productImageRepository;


    public Map<String,String> uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        String url = uploadResult.get("url").toString();
        String publicId = uploadResult.get("public_id").toString();
        return Map.of(
                "url", url,
                "public_id", publicId
        );
    }

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        Map options = ObjectUtils.asMap("folder", folder);
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return uploadResult.get("url").toString();
    }

    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public void deleteImageFromCloudinary(List<ProductImageEntity> imagenes) {
        for (ProductImageEntity imagen : imagenes) {
            try {
                // Eliminar de Cloudinary
                if (imagen.getPublicId() != null && !imagen.getPublicId().isEmpty()) {
                    Map result = cloudinary.uploader().destroy(imagen.getPublicId(), ObjectUtils.emptyMap());
                    System.out.println("Imagen eliminada de Cloudinary: " + result);
                }

                // Eliminar de la base de datos
                productImageRepository.delete(imagen);
            } catch (IOException e) {
                // Loggear el error pero continuar con las demás imágenes
                System.err.println("Error al eliminar imagen de Cloudinary: " + e.getMessage());
                // Podrías lanzar una excepción personalizada si lo prefieres
            }
        }
    }

}
