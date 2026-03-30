package com.osama_farag.money_manager.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.osama_farag.money_manager.dto.IncomeDTO;
import com.osama_farag.money_manager.entity.CategoryEntity;
import com.osama_farag.money_manager.entity.IncomeEntity;
import com.osama_farag.money_manager.entity.ProfileEntity;
import com.osama_farag.money_manager.repository.CategoryRepository;
import com.osama_farag.money_manager.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeService {


    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;

    public IncomeDTO addIncome(IncomeDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
        IncomeEntity income = toEntity(dto, profile, category);
        IncomeEntity newIncome = incomeRepository.save(income);
        return toDTO(newIncome);
    }



    //helper methods
    private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity category) {
        return IncomeEntity.builder()
            .name(dto.getName())
            .icon(dto.getIcon())
            .amount(dto.getAmount())
            .date(dto.getDate() != null ? dto.getDate() : LocalDate.now())
            .profile(profile)
            .category(category)
            .build();
    }


    private IncomeDTO toDTO(IncomeEntity entity) {
        return IncomeDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .icon(entity.getIcon())
            .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
            .categoryName(entity.getCategory() != null ? entity.getCategory().getName(): "N/A")
            .amount(entity.getAmount())
            .date(entity.getDate() != null ? entity.getDate() : java.time.LocalDate.now())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }


}