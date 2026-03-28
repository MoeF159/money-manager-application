package com.osama_farag.money_manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osama_farag.money_manager.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{

    //select * from tbl_categories where profile_id = ?
    List<CategoryEntity> findByProfileId(Long profileId);
    
    //select * from tbl_categories where id = ? and profile_id = ?
    Optional<CategoryEntity> findByIdAndProfileId(Long id, Long profileId);

    //select * from tbl_categories where type = ? and profile_id = ?
    List<CategoryEntity> findByTypeAndProfileId(String type, Long profileId);

    //select * from tbl_categories where name = ? and profile_id = ?
    Boolean existsByNameAndProfileId(String name, Long profileId);

}