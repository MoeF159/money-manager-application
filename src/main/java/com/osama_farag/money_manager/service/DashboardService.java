package com.osama_farag.money_manager.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Stream.concat;

import org.springframework.stereotype.Service;

import com.osama_farag.money_manager.dto.ExpenseDTO;
import com.osama_farag.money_manager.dto.IncomeDTO;
import com.osama_farag.money_manager.dto.RecentTransationDTO;
import com.osama_farag.money_manager.entity.ProfileEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String, Object> getDashboardData(){
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDTO> latestExpenses = expenseService.getLatest5ExpensesForCurrentUser();
        List<RecentTransationDTO> recentTransactions = concat(
            latestIncomes.stream().map((IncomeDTO income) -> 
                RecentTransationDTO.builder()
                    .id(income.getId())
                    .profileId(profile.getId())
                    .icon(income.getIcon())
                    .name(income.getName())
                    .amount(income.getAmount())
                    .date(income.getDate())
                    .createdAt(income.getCreatedAt())
                    .updatedAt(income.getUpdatedAt())
                    .type("income")
                    .build()), 
            latestExpenses.stream().map((ExpenseDTO expense) -> 
                RecentTransationDTO.builder()
                    .id(expense.getId())
                    .profileId(profile.getId())
                    .icon(expense.getIcon())
                    .name(expense.getName())
                    .amount(expense.getAmount())
                    .date(expense.getDate())
                    .createdAt(expense.getCreatedAt())
                    .updatedAt(expense.getUpdatedAt())
                    .type("expense")
                    .build()
                    )).sorted((a, b) -> {
                        int comp = b.getDate().compareTo(a.getDate());
                        if(comp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null){
                            return b.getCreatedAt().compareTo(a.getCreatedAt());
                        }
                        return comp;
                    }).collect(Collectors.toList());
        returnValue.put(
            "totalBalance", 
            incomeService.getTotalIncomeForCurrentUser()
            .subtract(expenseService.getTotalExpenseForCurrentUser())
        );
        returnValue.put(
            "totalIncome", 
            incomeService.getTotalIncomeForCurrentUser()
        );
        returnValue.put(
            "totalExpense", 
            expenseService.getTotalExpenseForCurrentUser()
        );
        returnValue.put(
            "recent5Expenses",
            latestExpenses
        );
        returnValue.put(
            "recent5Incomes",
            latestIncomes
        );
        returnValue.put(
            "recentTransactions",
            recentTransactions
        );
        return returnValue;
    }
    
}