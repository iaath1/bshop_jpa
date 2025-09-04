package com.bshop_jpa.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bshop_jpa.demo.repositories.ProductRepository;

@Controller
@RequestMapping("/store")
public class StoreController {

    private final ProductRepository productRepo;

    public StoreController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping
    public String getStore(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "store";
    }
}
