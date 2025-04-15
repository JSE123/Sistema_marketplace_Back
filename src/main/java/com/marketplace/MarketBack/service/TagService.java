package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.controller.dto.TagDTO;
import com.marketplace.MarketBack.persistence.entity.TagEntity;
import com.marketplace.MarketBack.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    public TagEntity createTag(TagDTO tagDTO) {
        TagEntity tag = TagEntity.builder().name(tagDTO.name()).build();
        return tagRepository.save(tag);
    }

    public List<TagEntity> getAllTags() {
        return tagRepository.findAll();
    }
}
