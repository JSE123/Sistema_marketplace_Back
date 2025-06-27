package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.CategoryDTO;
import com.marketplace.MarketBack.persistence.entity.CategoryEntity;
import com.marketplace.MarketBack.service.CategoryService;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    //Create a new category
    @PostMapping
    public ResponseEntity<CategoryEntity> createCategory(@RequestBody CategoryDTO categoryName){
         CategoryEntity category = categoryService.create(categoryName);
         return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    // Get all categories
    @GetMapping
    public List<CategoryEntity> getCategories(){
        return categoryService.getAllCategories();
    }


    //Delete category by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Eliminado correctamente");
    }

    //Update category
    @PutMapping("{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id,@RequestBody CategoryDTO category){
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
//        return ResponseEntity.ok("funciona");
    }
}
