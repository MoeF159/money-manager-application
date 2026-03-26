package com.osama_farag.money_manager.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    
    private Long id;
    private String profileId;
    private String name;
    private String icon;
    private String type; // "income" or "expense"   
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}