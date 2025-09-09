package com.bshop_jpa.demo.controllers;
import com.bshop_jpa.demo.services.OrderService;
import com.bshop_jpa.demo.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.models.Color;
import com.bshop_jpa.demo.models.Material;
import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.models.Role;
import com.bshop_jpa.demo.models.Size;
import com.bshop_jpa.demo.models.Status;
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

    @GetMapping("/products/add")
    public String getAdminPanelProductAdd(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("sizes", sizeRepo.findAll());
        model.addAttribute("colors", colorRepo.findAll());
        model.addAttribute("materials", materialRepo.findAll());
        model.addAttribute("categories", categoryRepo.findAll());
        return "admin/adminPanelProductsAdd";
    }

    @PostMapping("/products/add")
    public String postAdminPanelProductAdd(Model model, @ModelAttribute Product product, @RequestParam MultipartFile imageFile) throws IOException {

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
        product.setImageUrl("/uploads/products/" + fileName);
    }

    productRepo.save(product);
    return "redirect:/admin/products";
    }

    @GetMapping("/orders")
    public String getAdminPanelOrders(Model model) {
        model.addAttribute("orders", orderRepo.findAll());
        return "admin/adminPanelOrders";
    }



    @GetMapping("/categories")
    public String getAdminPanelCategories(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("categories", productRepo.countProductsByCategory());
        return "admin/adminPanelCategories";
    }

    @PostMapping("/categories/add")
    public String postAdminPanelCategoriesAdd(Model model, @ModelAttribute Category category) {
        if(categoryRepo.existsByName(category.getName())) {
            model.addAttribute("error", "Category with this name already exists");
            return "redirect:/admin/categories";
        }

        categoryRepo.save(category);
        return "redirect:/admin/categories";
    }



    @GetMapping("/sizes")
    public String getAdminPanelSizes(Model model) {
        model.addAttribute("newSize", new Size());
        model.addAttribute("sizes", productRepo.countProductsBySize());
        return "admin/adminPanelSizes";
    }

    @PostMapping("/sizes/add")
    public String postAdminPanelSizesAdd(Model model, @ModelAttribute Size size) {
        if(sizeRepo.existsByName(size.getName())) {
            model.addAttribute("error", "Size with this name already exists");
            return "redirect:/admin/sizes";
        }

        sizeRepo.save(size);
        return "redirect:/admin/sizes";
    }



    @GetMapping("/colors")
    public String getAdminPanelColors(Model model) {
        model.addAttribute("newColor", new Color());
        model.addAttribute("colors", productRepo.countProductsByColor());
        return "admin/adminPanelColors";
    }

    @PostMapping("/colors/add")
    public String postAdminPanelColorsAdd(Model model, @ModelAttribute Color color) {
        if(colorRepo.existsByName(color.getName())) {
            model.addAttribute("error", "Color with this name already exists");
            return "redirect:/admin/colors";
        }

        colorRepo.save(color);
        return "redirect:/admin/colors";
    }



    @GetMapping("/materials")
    public String getAdminPageMaterials(Model model) {
        model.addAttribute("newMaterial", new Material());
        model.addAttribute("materials", productRepo.countProductsByMaterial());
        return "admin/adminPanelMaterials";
    }

    @PostMapping("/materials/add")
    public String postAdminPageMaterialAdd(Model model, @ModelAttribute Material material) {
        if(materialRepo.existsByName(material.getName())) {
            model.addAttribute("error", "Material with this name already exists");
            return "redirect:/admin/materials";
        }
        
        materialRepo.save(material);
        return "redirect:/admin/materials";
    }



    @GetMapping("/statuses")
    public String getAdminPanelStatuses(Model model) {
        model.addAttribute("newStatus", new Status());
        model.addAttribute("statuses", orderRepo.countProductsByStatus());
        return "admin/adminPanelStatuses";
    }

    @PostMapping("/statuses/add")
    public String postAdminPanelStatusesAdd(Model model, @ModelAttribute Status status) {
        if(statusRepo.existsByName(status.getName())) {
            model.addAttribute("error", "Status with this name already exists");
            return "redirect:/admin/statuses";
        }

        statusRepo.save(status);
        return "redirect:/admin/statuses";
    }



    @GetMapping("/roles")
    public String getAdminPanelRoles(Model model) {
        model.addAttribute("newRole", new Role());
        model.addAttribute("roles", userRepo.countUsersByRole());
        return "admin/adminPanelRoles";
    }

    @PostMapping("/roles/add")
    public String postAdminPanelRolesAdd(Model model, @ModelAttribute Role role) {
        if(roleRepo.existsByName(role.getName())) {
            model.addAttribute("error", "Role with this name already exists");
            return "redirect:/admin/roles";
        }

        roleRepo.save(role);
        return "redirect:/admin/roles";
    }


}
