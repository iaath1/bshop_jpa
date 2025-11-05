package com.bshop_jpa.demo.controllers;
import com.bshop_jpa.demo.services.ColorService;
import com.bshop_jpa.demo.services.MaterialService;
import com.bshop_jpa.demo.services.OrderService;
import com.bshop_jpa.demo.services.ProductService;
import com.bshop_jpa.demo.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.models.Color;
import com.bshop_jpa.demo.models.Image;
import com.bshop_jpa.demo.models.Material;
import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.models.Role;
import com.bshop_jpa.demo.models.Status;
import com.bshop_jpa.demo.services.CategoryService;
import com.bshop_jpa.demo.repositories.ImageRepository;
import com.bshop_jpa.demo.repositories.RoleRepository;
import com.bshop_jpa.demo.services.SizeService;
import com.bshop_jpa.demo.services.StatusService;
import com.bshop_jpa.demo.repositories.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final String IMAGE_PATH = "/uploads/products/";

    @Autowired
    private final UserService userService;
    
    private final UserRepository userRepo;
    private final ProductService productService;
    private final ColorService colorService;
    private final MaterialService materialService;
    private final RoleRepository roleRepo;
    private final SizeService sizeService;
    private final StatusService statusService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final ImageRepository imageRepo;

    public AdminController(UserRepository userRepo, ProductService productService, ColorService colorService, MaterialService materialService, 
                            RoleRepository roleRepo, SizeService sizeService, StatusService statusService, CategoryService categoryService,
                            OrderService orderService, UserService userService, ImageRepository imageRepo) {

        this.userRepo = userRepo;
        this.productService = productService;
        this.colorService = colorService;
        this.materialService = materialService;
        this.roleRepo = roleRepo;
        this.sizeService = sizeService;
        this.statusService = statusService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.orderService = orderService;
        this.imageRepo = imageRepo;
    }




    @GetMapping
    public String getAdminPanel(Model model) {
        model.addAttribute("totalUsers", userRepo.count());
        model.addAttribute("totalProducts", productService.countAllProduct());
        model.addAttribute("totalOrders", orderService.countOrders());
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
    public String getAdminPanelProducts(Model model, @RequestParam(required = false) String search,
        @RequestParam(required = false) Integer colorId,
        @RequestParam(defaultValue = "name") String sortBy,
        @RequestParam(defaultValue = "asc") String order,
        @RequestParam(required = false) Integer categoryId,
        @RequestParam(required = false) String sizeName,
        @RequestParam(required = false) Integer materialId) {

        List<Product> products = productService.getFilteredProducts(search, categoryId, colorId, materialId, sizeName, order);

        Map<String, Object> params = new HashMap<>();
        params.put("search", search);
        params.put("colorId", colorId);
        params.put("sortBy", sortBy);
        params.put("order", order);
        params.put("categoryId", categoryId);
        params.put("sizeName", sizeName);
        params.put("materialId", materialId);

        model.addAttribute("products", products);
        model.addAttribute("parameters", params);
        model.addAttribute("colors", colorService.findAllColors());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("materials", materialService.findAllMaterials());

        return "admin/adminPanelProducts";
    }

    @GetMapping("/products/add")
    public String getAdminPanelProductAdd(Model model) {
        Product product = new Product();
        product.setSizes(productService.setBlankSizes(product));

        model.addAttribute("product", product);
        model.addAttribute("colors", colorService.findAllColors());
        model.addAttribute("materials", materialService.findAllMaterials());
        model.addAttribute("categories", categoryService.findAllCategories());
        return "admin/adminPanelProductsAdd";
    }

    @PostMapping("/products/add")
    public String postAdminPanelProductAdd(Model model, @ModelAttribute Product product, @RequestParam(name = "files") MultipartFile[] imageFiles) throws IOException {

        List<Image> images = new ArrayList<>();

        if (imageFiles.length != 0) {
            // Папка, где будут храниться картинки (рядом с pom.xml)
            String uploadDir = System.getProperty("user.dir") + "/uploads/products/";

            // Создать папку, если её нет
            Files.createDirectories(Paths.get(uploadDir));

        
            for(MultipartFile imageFile : imageFiles) {
                // Уникальное имя файла
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

                // Полный путь до файла
                Path filePath = Paths.get(uploadDir, fileName);
                Files.write(filePath, imageFile.getBytes());

                // В БД сохраняем только путь для web
                images.add(new Image(product, "/uploads/products/" + fileName));
            }
        }
        product.setImages(images);
        productService.saveNewProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/update/{id}")
    public String getAdminPanelProductsUpdate(@PathVariable Long id, Model model) {
        Product product = productService.sortProductSizes(productService.findProductById(id));
        product.getImages().removeIf(img -> img.getImageUrl() == null || img.getImageUrl().isBlank());

        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("materials", materialService.findAllMaterials());
        model.addAttribute("colors", colorService.findAllColors());
        model.addAttribute("product", product);
        return "admin/adminPanelProductsUpdate";
    }

    @PostMapping("/products/update/{id}")
    public String postAdminPanelProductsUpdate(
        Model model,
        @ModelAttribute Product product,
        @RequestParam(name = "files", required = false) MultipartFile[] imageFiles,
        @PathVariable Long id) throws IOException {

        Product existing = productService.findProductById(id);

        if(existing == null) {throw new RuntimeException("Product with id: " + id + " Does not exists.");}
                

        // Обновляем основные поля
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());
        existing.setColor(product.getColor());
        existing.setMaterial(product.getMaterial());
        existing.setCategory(product.getCategory());

        // Папка для загрузки
        String uploadDir = System.getProperty("user.dir") + "/uploads/products/";
        Files.createDirectories(Paths.get(uploadDir));

        // ✅ Добавляем новые изображения, не удаляя старые
        if (imageFiles != null && imageFiles.length > 0) {
            for (MultipartFile imageFile : imageFiles) {
                if (imageFile.isEmpty()) continue;

                // Получаем оригинальное расширение
                String originalFilename = imageFile.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                // Уникальное имя файла
                String fileName = UUID.randomUUID() + extension;

                // Полный путь
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Добавляем в список
                Image image = new Image(existing, "/uploads/products/" + fileName);
                existing.getImages().add(image);
            }
        }

        productService.saveProduct(productService.updateProductSizesQuantity(existing, product.getSizes()));
        return "redirect:/admin/products";
    }



    @DeleteMapping("/products/image/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/products/delete/{id}")
    public String postAdminPanelProductsDelete(@PathVariable Long id) {
        productService.deleteProductById(id);

        return "redirect:/admin/products";
    }


    @GetMapping("/orders")
    public String getAdminPanelOrders(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        return "admin/adminPanelOrders";
    }



    @GetMapping("/categories")
    public String getAdminPanelCategories(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("categories", productService.countProductsByCategory());
        return "admin/adminPanelCategories";
    }

    @PostMapping("/categories/add")
    public String postAdminPanelCategoriesAdd(Model model, @ModelAttribute Category category) {
        if(categoryService.existsByName(category.getName())) {
            model.addAttribute("error", "Category with this name already exists");
            return "redirect:/admin/categories";
        }

        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }



    // @GetMapping("/sizes")
    // public String getAdminPanelSizes(Model model) {
    //     model.addAttribute("newSize", new Size());
    //     model.addAttribute("sizes", productService.countProductsBySize());
    //     return "admin/adminPanelSizes";
    // }

    // @PostMapping("/sizes/add")
    // public String postAdminPanelSizesAdd(Model model, @ModelAttribute Size size) {
    //     if(sizeService.existsByName(size.getName())) {
    //         model.addAttribute("error", "Size with this name already exists");
    //         return "redirect:/admin/sizes";
    //     }

    //     sizeService.save(size);
    //     return "redirect:/admin/sizes";
    // }



    @GetMapping("/colors")
    public String getAdminPanelColors(Model model) {
        model.addAttribute("newColor", new Color());
        model.addAttribute("colors", productService.countProductsByColor());
        return "admin/adminPanelColors";
    }

    @PostMapping("/colors/add")
    public String postAdminPanelColorsAdd(Model model, @ModelAttribute Color color) {
        if(colorService.existsByName(color.getName())) {
            model.addAttribute("error", "Color with this name already exists");
            return "redirect:/admin/colors";
        }

        colorService.saveColor(color);
        return "redirect:/admin/colors";
    }



    @GetMapping("/materials")
    public String getAdminPageMaterials(Model model) {
        model.addAttribute("newMaterial", new Material());
        model.addAttribute("materials", productService.countProductsByMaterial());
        return "admin/adminPanelMaterials";
    }

    @PostMapping("/materials/add")
    public String postAdminPageMaterialAdd(Model model, @ModelAttribute Material material) {
        if(materialService.existsByName(material.getName())) {
            model.addAttribute("error", "Material with this name already exists");
            return "redirect:/admin/materials";
        }
        
        materialService.saveMaterial(material);
        return "redirect:/admin/materials";
    }



    @GetMapping("/statuses")
    public String getAdminPanelStatuses(Model model) {
        model.addAttribute("newStatus", new Status());
        model.addAttribute("statuses", orderService.countOrdersByStatus());
        return "admin/adminPanelStatuses";
    }

    @PostMapping("/statuses/add")
    public String postAdminPanelStatusesAdd(Model model, @ModelAttribute Status status) {
        if(statusService.existsByName(status.getName())) {
            model.addAttribute("error", "Status with this name already exists");
            return "redirect:/admin/statuses";
        }

        statusService.saveStatus(status);
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



    private String saveMultiPartFile(MultipartFile imageFile) throws IOException{
        // Папка, где будут храниться картинки (рядом с pom.xml)
        String uploadDir = System.getProperty("user.dir") + "/uploads/products/";

        // Создать папку, если её нет
        Files.createDirectories(Paths.get(uploadDir));

        // Уникальное имя файла
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        // Полный путь до файла
        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, imageFile.getBytes());

        return fileName;
    }
}
