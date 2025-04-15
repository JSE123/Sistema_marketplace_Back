package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.TagDTO;
import com.marketplace.MarketBack.persistence.entity.TagEntity;
import com.marketplace.MarketBack.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    TagService tagService;

    //Create a new tag
    @PostMapping
    public ResponseEntity<TagEntity> createTag(@RequestBody TagDTO tagDTO){
        TagEntity tag = tagService.createTag(tagDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(tag);

    }

    //Get all tags
    @GetMapping
    public List<TagEntity> getTags(){
        return tagService.getAllTags();
    }
}
