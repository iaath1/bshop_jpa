package com.bshop_jpa.demo.controllers;


import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.repositories.RoleRepository;
import com.bshop_jpa.demo.repositories.UserRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    @GetMapping
    public String getAuth(Model model) {
        model.addAttribute("user", new User());
        return "authentitication";
    }

    @PostMapping()
    public String postAuth(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if(userRepo.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "User with this email already exists.");
            return "authentitication";
        }

        if(result.hasErrors()) {
            return "authentitication";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleRepo.findByName("ADMIN").get()));
        userRepo.save(user);

        return "redirect:/login";
    }

}
