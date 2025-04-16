package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.controller.dto.TagDTO;
import com.marketplace.MarketBack.exception.custom.NotFoundException;
import com.marketplace.MarketBack.exception.enums.ErrorCode;
import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.persistence.entity.TagEntity;
import com.marketplace.MarketBack.persistence.repository.ProductRepository;
import com.marketplace.MarketBack.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    @Autowired
    private ProductRepository productRepository;

    public TagEntity createTag(TagDTO tagDTO) {
        ProductEntity product = productRepository.findById(tagDTO.productId())
                .orElseThrow(() -> new NotFoundException("Producto no encontrado", ErrorCode.PRODUCT_NOT_FOUND));
        TagEntity tag = TagEntity.builder()
                .name(tagDTO.name())
                .product(product)
                .build();
        return tagRepository.save(tag);
    }

    public List<TagEntity> getAllTags() {
        return tagRepository.findAll();
    }
}
