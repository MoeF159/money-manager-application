package com.osama_farag.money_manager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.osama_farag.money_manager.dto.ExpenseDTO;
import com.osama_farag.money_manager.entity.CategoryEntity;
import com.osama_farag.money_manager.entity.ExpenseEntity;
import com.osama_farag.money_manager.entity.ProfileEntity;
import com.osama_farag.money_manager.repository.CategoryRepository;
import com.osama_farag.money_manager.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;

    public ExpenseDTO addExpense(ExpenseDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
        ExpenseEntity expense = toEntity(dto, profile, category);
        ExpenseEntity newExpense = expenseRepository.save(expense);
        return toDTO(newExpense);
    }

    //get all expenses for current month based on the start and end date of the month
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<ExpenseEntity> expenses = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return expenses.stream().map(this::toDTO).toList();
    }

    //delete expense by id for current user
    public void deleteExpense(Long expenseId){
        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenseEntity entity = expenseRepository.findById(expenseId)
            .orElseThrow(() -> new RuntimeException("Expense not found with id: " + expenseId));
        if(!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorized to delete this expense");
        }
        expenseRepository.delete(entity);
    }


    //Get latest 5 expenses for current user
    public List<ExpenseDTO> getLatest5ExpensesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> expenses = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return expenses.stream().map(this::toDTO).toList();
    }

    //Get total expenses for current user
    public BigDecimal getTotalExpenseForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal totalExpense = expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return totalExpense != null ? totalExpense : BigDecimal.ZERO;
    }

    //filter expenses
    public List<ExpenseDTO> filterExpenses(LocalDate starDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), starDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
    }

    //helper methods
    private ExpenseEntity toEntity(ExpenseDTO dto, ProfileEntity profile, CategoryEntity category) {
        return ExpenseEntity.builder()
            .name(dto.getName())
            .icon(dto.getIcon())
            .amount(dto.getAmount())
            .date(dto.getDate() != null ? dto.getDate() : LocalDate.now())
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
            .date(entity.getDate() != null ? entity.getDate() : java.time.LocalDate.now())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

}