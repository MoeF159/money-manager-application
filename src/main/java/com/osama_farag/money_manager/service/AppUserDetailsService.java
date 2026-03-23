package com.osama_farag.money_manager.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.osama_farag.money_manager.entity.ProfileEntity;
import com.osama_farag.money_manager.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileService profileService;
    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Load user details based on email for authentication
        ProfileEntity existingProfile = profileRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    
        // Return a UserDetails object with the user's email, password, and authorities
        return User.builder()
            .username(existingProfile.getEmail())
            .password(existingProfile.getPassword())
            .authorities(Collections.emptyList())
            .build();
    }
}