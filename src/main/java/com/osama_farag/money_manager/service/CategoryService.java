package com.osama_farag.money_manager.service;

import org.springframework.stereotype.Service;

import com.osama_farag.money_manager.dto.CategoryDTO;
import com.osama_farag.money_manager.entity.CategoryEntity;
import com.osama_farag.money_manager.entity.ProfileEntity;
import com.osama_farag.money_manager.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

  
  
  
  
    //helper methods
    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile){
        return CategoryEntity.builder()
            .name(categoryDTO.getName())
            .icon(categoryDTO.getIcon())
            .profile(profile)
            .type(categoryDTO.getType())
            .build();
    }

    private CategoryDTO toDTO(CategoryEntity entity){
        return CategoryDTO.builder()
            .id(entity.getId())
            .profileId(entity.getProfile() != null ? entity.getProfile().getId(): null)
            .name(entity.getName())
            .icon(entity.getIcon())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .type(entity.getType())
            .build();
    }

}