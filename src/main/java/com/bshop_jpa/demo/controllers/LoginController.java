package com.bshop_jpa.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private UserService userService;
    private AuthenticationManager authManager;

    @GetMapping
    public String getLogin(@RequestParam(value = "error", required = false) String error, Model model, HttpServletRequest request) {

        if(error != null) {
            model.addAttribute("error", "Incorrect email or password.");
        }

        model.addAttribute("currentUrl", request.getRequestURI());
        return "login";
    }

    // @PostMapping
    // public String postLogin(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
    //     User userFromDb = userService.findByEmail(username);
    //     boolean isPasswordEquals = passwordEncoder.matches(password, userFromDb.getPassword());

    //     if(isPasswordEquals && userFromDb.isActive()) {
    //         authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));   
    //     }

    //     return "redirect:/store";
    // }
    

}
