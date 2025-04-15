package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.controller.dto.CategoryDTO;
import com.marketplace.MarketBack.persistence.entity.CategoryEntity;
import com.marketplace.MarketBack.persistence.repository.CateoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CateoryRepository categoryRepository;

    public CategoryEntity create(CategoryDTO categoryName) {
        CategoryEntity category = CategoryEntity.builder().name(categoryName.name()).build();
        return categoryRepository.save(category);
    }

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }
}
