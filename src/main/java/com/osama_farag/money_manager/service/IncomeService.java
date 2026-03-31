package com.osama_farag.money_manager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
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

    //get all incomes for current month based on the start and end date of the month
    public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> incomes = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return incomes.stream().map(this::toDTO).toList();
    }

    //delete income by id for current user
    public void deleteIncome(Long incomeId){
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity entity = incomeRepository.findById(incomeId)
            .orElseThrow(() -> new RuntimeException("Income not found with id: " + incomeId));
        if(!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorized to delete this income");
        }
        incomeRepository.delete(entity);
    }

    //Get latest 5 incomes for current user
    public List<IncomeDTO> getLatest5IncomesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> incomes = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return incomes.stream().map(this::toDTO).toList();
    }

    //Get total incomes for current user
    public BigDecimal getTotalIncomeForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal totalIncome = incomeRepository.findTotalIncomeByProfileId(profile.getId());
        return totalIncome != null ? totalIncome : BigDecimal.ZERO;
    }

    //filter incomes
    public List<IncomeDTO> filterIncomes(LocalDate starDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), starDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
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