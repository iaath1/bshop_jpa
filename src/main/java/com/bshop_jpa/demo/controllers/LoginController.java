package com.bshop_jpa.demo.controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.services.UserService;

@Controller
@RequestMapping("/login")
public class LoginController {
    
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public LoginController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        if(error != null) {
            model.addAttribute("error", "Incorrect email or password.");
        }

        return "login";
    }

//     @PostMapping
//     public String postLogin(@ModelAttribute("user") @Valid User user, Model model, HttpSession session) {

//         User found = userService.findByEmail(user.getEmail());

//         if(found == null || !passwordEncoder.matches(user.getPassword(), found.getPassword())) {
//             model.addAttribute("error", "Incorrect email or password.");
//             return "login";
//         }

//         session.setAttribute("loggedUser", found);

//         return "redirect:/store";
// }

}
