package com.osama_farag.money_manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osama_farag.money_manager.entity.ProfileEntity;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long>{
    
    // select * from tbl_profiles where email = ?
    Optional<ProfileEntity> findByEmail(String email);

    // select * from tbl_profiles where activation_token = ?
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}