package com.bshop_jpa.demo.controllers;
import com.bshop_jpa.demo.services.OrderService;
import com.bshop_jpa.demo.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bshop_jpa.demo.repositories.CategoryRepository;
import com.bshop_jpa.demo.repositories.ColorRepository;
import com.bshop_jpa.demo.repositories.MaterialRepository;
import com.bshop_jpa.demo.repositories.OrderRepository;
import com.bshop_jpa.demo.repositories.ProductRepository;
import com.bshop_jpa.demo.repositories.RoleRepository;
import com.bshop_jpa.demo.repositories.SizeRepository;
import com.bshop_jpa.demo.repositories.StatusRepository;
import com.bshop_jpa.demo.repositories.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;

    @Autowired
    private final UserService userService;
    
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final ColorRepository colorRepo;
    private final MaterialRepository materialRepo;
    private final RoleRepository roleRepo;
    private final SizeRepository sizeRepo;
    private final StatusRepository statusRepo;
    private final CategoryRepository categoryRepo;
    private final OrderRepository orderRepo;

    public AdminController(UserRepository userRepo, ProductRepository productRepo, ColorRepository colorRepo, MaterialRepository materialRepo, 
                            RoleRepository roleRepo, SizeRepository sizeRepo, StatusRepository statusRepo, CategoryRepository categoryRepo, OrderRepository orderRepo, UserService userService, OrderService orderService) {

        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.colorRepo = colorRepo;
        this.materialRepo = materialRepo;
        this.roleRepo = roleRepo;
        this.sizeRepo = sizeRepo;
        this.statusRepo = statusRepo;
        this.categoryRepo = categoryRepo;
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    public String getAdminPanel(Model model) {
        model.addAttribute("totalUsers", userRepo.count());
        model.addAttribute("totalProducts", productRepo.count());
        model.addAttribute("totalOrders", orderRepo.count());
        model.addAttribute("recentUsers", userService.findRecentUsers(5));
        model.addAttribute("recentOrders", orderService.findRecentOrders(5));
        return "admin/adminPanel";
    }

    @GetMapping("/users")
    public String getAdminPanelUsers(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "admin/adminPanelUsers";
    }

    @GetMapping("/products")
    public String getAdminPanelProducts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "admin/adminPanelProducts";
    }

    @GetMapping("/orders")
    public String getAdminPanelOrders(Model model) {
        model.addAttribute("orders", orderRepo.findAll());
        return "admin/adminPanelOrders";
    }

    @GetMapping("/categories")
    public String getAdminPanelCategories(Model model) {
        model.addAttribute("categories", productRepo.countProductsByCategory());
        return "admin/adminPanelCategories";
    }

    @GetMapping("/sizes")
    public String getAdminPanelSizes(Model model) {
        model.addAttribute("sizes", productRepo.countProductsBySize());
        return "admin/adminPanelSizes";
    }

    @GetMapping("/colors")
    public String getAdminPanelColors(Model model) {
        model.addAttribute("colors", productRepo.countProductsByColor());
        return "admin/adminPanelColors";
    }

    @GetMapping("/materials")
    public String getAdminPageMaterials(Model model) {
        model.addAttribute("materials", productRepo.countProductsByMaterial());
        return "admin/adminPanelMaterials";
    }

    @GetMapping("/statuses")
    public String getAdminPanelStatuses(Model model) {
        model.addAttribute("statuses", orderRepo.countProductsByStatus());
        return "admin/adminPanelStatuses";
    }

    @GetMapping("/roles")
    public String getAdminPanelRoles(Model model) {
        model.addAttribute("roles", userRepo.countUsersByRole());
        return "admin/adminPanelRoles";
    }

}
