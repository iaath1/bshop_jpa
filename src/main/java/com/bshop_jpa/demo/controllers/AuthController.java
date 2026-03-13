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
import org.springframework.web.bind.annotation.RequestParam;

import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.repositories.RoleRepository;
import com.bshop_jpa.demo.repositories.UserRepository;
import com.bshop_jpa.demo.services.ConfirmationEmailService;
import com.bshop_jpa.demo.services.UserService;
import com.bshop_jpa.demo.services.VerificationCodeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    
    private final VerificationCodeService codeService;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationEmailService emailService;

    public AuthController(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepo, ConfirmationEmailService emailService, VerificationCodeService codeService, UserService userService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.emailService = emailService;
        this.codeService = codeService;
        this.userService = userService;
    }

    @GetMapping
    public String getAuth(Model model, HttpServletRequest request) {
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("user", new User());
        return "authentitication";
    }

    @PostMapping
    public String postAuth(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if(userRepo.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "User with this email already exists.");
            return "authentitication";
        }

        if(result.hasErrors()) {
            return "authentitication";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleRepo.findByName("ROLE_ADMIN").get()));
        user.setActive(false);
        userRepo.save(user);

        String code = codeService.generateCode();

        codeService.saveCode(user.getEmail(), code);
        emailService.sendConfirmationEmail(user.getEmail(), code);
        model.addAttribute("email", user.getEmail());
        return "verificationPage";
    }

    @GetMapping("/verify")
    public String getVerificationPage(@ModelAttribute("email") String email, Model model) {

        model.addAttribute("email", email);
        return "verificationPage";
    }

    @PostMapping("/verify")
    public String postVerificationPage(@RequestParam String email, @RequestParam String code, Model model) {
        boolean isVerified = codeService.verifyCode(email, code);
        User userToBeVerified = userService.findByEmail(email);

        if(isVerified) {
            userToBeVerified.setActive(true);
            userService.saveUser(userToBeVerified);
            System.out.println("Code is verified");
            return "redirect:/login";
        }

        userService.deleteUser(userToBeVerified);

        model.addAttribute("error", "Invalid verification code");
        model.addAttribute("email", email);
        return "redirect:/auth/verify";

    }
}
