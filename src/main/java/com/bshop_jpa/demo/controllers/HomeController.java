package com.bshop_jpa.demo.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.services.OrderService;
import com.bshop_jpa.demo.services.ProductService;

@Controller
@RequestMapping("/")
public class HomeController {
    private final ProductService productService;
    private final OrderService orderService;

    public HomeController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping
    public String getHome(Model model, Locale locale) throws Exception {
        List<Product> products = productService.getNewProducts(10)
        .stream()
        .map(p -> productService.localizateProduct(productService.sortProductSizes(p), locale.getLanguage()))
        .toList();

        model.addAttribute("lang", locale.getLanguage());
        model.addAttribute("newProducts", products);
        model.addAttribute("categories", productService.countProductsByCategory());
        model.addAttribute("totalProducts", productService.countAllProduct());
        model.addAttribute("totalOrders", orderService.countOrders());
        return "store/home";
    }
}
