package com.bshop_jpa.demo.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.services.UserService;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);

        return "user/profile";
    }

    @GetMapping("/profile/update")
    public String getProfileUpdate(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);

        return "user/userUpdate";
    }

    @PostMapping("/profile/update")
    public String postProfileUpdate(@AuthenticationPrincipal User currentUser, @ModelAttribute User user, Model model, @RequestParam(required = false) MultipartFile imageFile) throws IOException {

        

        currentUser.setName(user.getName());
        currentUser.setSurname(user.getSurname());
        currentUser.setPhone(user.getPhone());
        currentUser.setAddress(user.getAddress());

        if (!imageFile.isEmpty()) {
            // Папка, где будут храниться картинки (рядом с pom.xml)
            String uploadDir = System.getProperty("user.dir") + "/uploads/products/";

            // Создать папку, если её нет
            Files.createDirectories(Paths.get(uploadDir));

            // Уникальное имя файла
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

            // Полный путь до файла
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, imageFile.getBytes());

            // В БД сохраняем только путь для web
            currentUser.setAvatarUrl("/uploads/products/" + fileName);
        }

        userService.saveUser(currentUser);
        return "redirect:/user/profile";

    }

    
}
