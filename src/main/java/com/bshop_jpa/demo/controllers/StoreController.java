package com.bshop_jpa.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.repositories.CategoryRepository;
import com.bshop_jpa.demo.repositories.ProductRepository;

@Controller
@RequestMapping("/store")
public class StoreController {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public StoreController(ProductRepository productRepo, CategoryRepository categoryRepo)  {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public String getStore(Model model) {
        model.addAttribute("categories", productRepo.countProductsByCategory());
        model.addAttribute("products", productRepo.findAll());
        return "store/store";
    }

    @GetMapping("/category/{id}")
    public String getStoreCategoryDetails(@PathVariable Integer id, Model model) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException());
        model.addAttribute("category", category);
        model.addAttribute("products", productRepo.findByCategory(category));
        return "store/categoryDetails";
    }

    @GetMapping("/product/{id}")
    public String getStoreProductDetails(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException());
        model.addAttribute("product", product);
        return "store/productDetails";
    }


}
