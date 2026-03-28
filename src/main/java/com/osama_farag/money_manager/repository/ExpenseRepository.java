package com.osama_farag.money_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osama_farag.money_manager.entity.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>{

}