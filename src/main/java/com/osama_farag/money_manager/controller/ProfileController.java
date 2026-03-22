package com.osama_farag.money_manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.osama_farag.money_manager.dto.ProfileDTO;
import com.osama_farag.money_manager.service.ProfileService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class ProfileController {
    
   private final ProfileService profileService;
   
   @PostMapping("/register")
   public ResponseEntity<ProfileDTO> registerProfile (@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registeredProfile = profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }
}