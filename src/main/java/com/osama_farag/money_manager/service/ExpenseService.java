package com.osama_farag.money_manager.service;

import org.springframework.stereotype.Service;

import com.osama_farag.money_manager.dto.ExpenseDTO;
import com.osama_farag.money_manager.entity.CategoryEntity;
import com.osama_farag.money_manager.entity.ExpenseEntity;
import com.osama_farag.money_manager.entity.ProfileEntity;
import com.osama_farag.money_manager.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryService categoryService;
    private final ExpenseRepository expenseRepository;




    //helper methods
    private ExpenseEntity toEntity(ExpenseDTO dto, ProfileEntity profile, CategoryEntity category) {
        return ExpenseEntity.builder()
            .name(dto.getName())
            .icon(dto.getIcon())
            .amount(dto.getAmount())
            .date(dto.getDate())
            .profile(profile)
            .category(category)
            .build();
    }


    private ExpenseDTO toDTO(ExpenseEntity entity) {
        return ExpenseDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .icon(entity.getIcon())
            .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
            .categoryName(entity.getCategory() != null ? entity.getCategory().getName(): "N/A")
            .amount(entity.getAmount())
            .date(entity.getDate())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

}