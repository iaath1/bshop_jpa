package com.bshop_jpa.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User dbUser = userRepo.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User was not found: " + email));

        return new CustomUserDetails(dbUser);
    }
    
}
