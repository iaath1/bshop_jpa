package com.bshop_jpa.demo.controllers;


import com.bshop_jpa.demo.repositories.SizeRepository;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final SizeRepository sizeRepository;

    private final UserService userService;
    
    private final VerificationCodeService codeService;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationEmailService emailService;

    public AuthController(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepo, ConfirmationEmailService emailService, VerificationCodeService codeService, UserService userService, SizeRepository sizeRepository) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.emailService = emailService;
        this.codeService = codeService;
        this.userService = userService;
        this.sizeRepository = sizeRepository;
    }

    @GetMapping
    public String getAuth(Model model, HttpServletRequest request) {
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("user", new User());
        return "auth/authentitication";
    }

    @PostMapping
    public String postAuth(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if(userRepo.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "User with this email already exists.");
            return "auth/authentitication";
        }

        if(result.hasErrors()) {
            return "auth/authentitication";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleRepo.findByName("ROLE_CUSTOMER").get()));
        user.setActive(false);
        userRepo.save(user);

        String code = codeService.generateCode();

        codeService.saveCode(user.getEmail(), code);
        emailService.sendConfirmationEmail(user.getEmail(), code);
        model.addAttribute("email", user.getEmail());
        return "auth/verificationPage";
    }

    @GetMapping("/verify")
    public String getVerificationPage(@ModelAttribute("email") String email, Model model) {

        model.addAttribute("email", email);
        return "auth/verificationPage";
    }

    @PostMapping("/verify")
    public String postVerificationPage(@RequestParam String email, @RequestParam String code, Model model) {
        boolean isVerified = codeService.verifyCode(email, code);
        User userToBeVerified = userService.findByEmail(email);

        if(isVerified && userToBeVerified != null) {
            userToBeVerified.setActive(true);
            userService.saveUser(userToBeVerified);
            log.info("Email verified for user: {}", userToBeVerified.getEmail());
            return "redirect:/login";
        }


        model.addAttribute("error", "Invalid verification code");
        model.addAttribute("email", email);
        return "redirect:/auth/verify";

    }

    @GetMapping("/forget-password")
    public String getForgetPassword(Model model) {
        return "auth/forget-password";
    }

    @PostMapping("/forget-password")
    public String postForgetPassword(@RequestParam("email") String email, Model model) {

        if(!userRepo.existsByEmail(email)) {
            model.addAttribute("error", "No user with this email exists.");
            return "auth/forget-password";
        }

        String code = codeService.generateCode();
        codeService.saveCode(email, code);
        log.debug("Generated password reset code for: {}", email);

        emailService.sendConfirmationEmail(email, code);

        model.addAttribute("email", email);

        return "auth/password-forget-verify";
    }

    @GetMapping("/password-forget-verify")
    public String getForgetPasswordVerification() {
        return "auth/password-forget-verify";
    }

    @PostMapping("/password-forget-verify")
    public String postForgetPasswordVerification(@RequestParam("email") String email, @RequestParam("code") String code, Model model) {

        log.debug("Password reset verification attempt for: {}", email);
        boolean isVerified = codeService.verifyCode(email, code);
        model.addAttribute("email", email);

        if(!isVerified) {
            model.addAttribute("error", "Invalid verification code.");
            return "auth/password-forget-verify";
        }


        return "auth/changePassword";

    }

    @PostMapping("/password-change")
    public String passwordChange(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("password2") String password2, Model model) {

        model.addAttribute("email", email);

        if(!password.equals(password2)) {
            model.addAttribute("error", "Passwords are not the same.");
            return "auth/changePassword";
        }

        if(password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            return "auth/changePassword";
        }

        User user = userService.findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userService.saveUser(user);
        log.info("Password changed for user: {}", email);
        return "auth/login";
    }

}
