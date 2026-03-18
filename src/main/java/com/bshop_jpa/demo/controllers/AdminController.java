package com.bshop_jpa.demo.controllers;
import com.bshop_jpa.demo.services.ColorService;
import com.bshop_jpa.demo.services.ConfirmationEmailService;
import com.bshop_jpa.demo.services.ImageService;
import com.bshop_jpa.demo.services.MaterialService;
import com.bshop_jpa.demo.services.OrderService;
import com.bshop_jpa.demo.services.ProductService;
import com.bshop_jpa.demo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.models.Color;
import com.bshop_jpa.demo.models.Image;
import com.bshop_jpa.demo.models.Material;
import com.bshop_jpa.demo.models.Order;
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

    private final ConfirmationEmailService confirmationEmailService;

    @Value("${app.upload.dir}")
    private String MEDIA_PATH;

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
    private final ImageService imageService;

    public AdminController(UserRepository userRepo, ProductService productService, ColorService colorService, MaterialService materialService, 
                            RoleRepository roleRepo, SizeService sizeService, StatusService statusService, CategoryService categoryService,
                            OrderService orderService, UserService userService, ImageService imageService, ConfirmationEmailService confirmationEmailService) {

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
        this.imageService = imageService;
        this.confirmationEmailService = confirmationEmailService;
    }




    @GetMapping
    public String getAdminPanel(Model model, HttpServletRequest request) {

        model.addAttribute("currentUrl", request.getRequestURI());
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
        @RequestParam(required = false) Integer materialId,
        Locale locale,
        HttpServletRequest request) {

        List<Product> products = productService.getFilteredProducts(search, categoryId, colorId, materialId, sizeName, order, locale);

        Map<String, Object> params = new HashMap<>();
        params.put("search", search);
        params.put("colorId", colorId);
        params.put("sortBy", sortBy);
        params.put("order", order);
        params.put("categoryId", categoryId);
        params.put("sizeName", sizeName);
        params.put("materialId", materialId);
        
        model.addAttribute("lang", locale.getLanguage());
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("products", products);
        model.addAttribute("parameters", params);
        model.addAttribute("colors", colorService.findAllColors());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("materials", materialService.findAllMaterials());

        return "admin/adminPanelProducts";
    }

    @GetMapping("/products/add")
    public String getAdminPanelProductAdd(Model model, Locale locale) {
        Product product = new Product();
        product.setSizes(productService.setBlankSizes(product));

        model.addAttribute("lang", locale.getLanguage());
        model.addAttribute("product", product);
        model.addAttribute("colors", colorService.findAllColors());
        model.addAttribute("materials", materialService.findAllMaterials());
        model.addAttribute("categories", categoryService.findAllCategories());
        return "admin/adminPanelProductsAdd";
    }

    @PostMapping("/products/add")
    public String postAdminPanelProductAdd(Model model,
     @ModelAttribute Product product,
     @RequestParam(name = "files") MultipartFile[] imageFiles,
     @RequestParam(name = "nameUk") String nameUk,
     @RequestParam(name = "descriptionUk") String descriptionUk,
     @RequestParam(name = "namePl") String namePl,
     @RequestParam(name = "descriptionPl") String descriptionPl) throws IOException {

        List<Image> images = new ArrayList<>();

        if (imageFiles.length != 0) {
            // Создать папку, если её нет
            Path uploadDir = Paths.get(MEDIA_PATH);
            Files.createDirectories(uploadDir);

        
            for(MultipartFile imageFile : imageFiles) {
                // Уникальное имя файла
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

                // Полный путь до файла
                Path filePath = uploadDir.resolve(fileName);
                Files.write(filePath, imageFile.getBytes());

                // В БД сохраняем только путь для web
                images.add(new Image(product, "/media/" + fileName));
            }
        }
        product.setImages(images);
        productService.saveNewProduct(product);
        productService.addTranslationToProduct(product, "uk", nameUk, descriptionUk);
        productService.addTranslationToProduct(product, "pl", namePl, descriptionPl);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/update/{id}")
    public String getAdminPanelProductsUpdate(@PathVariable Long id, Model model, Locale locale) {
        Product product = productService.sortProductSizes(productService.findProductById(id));
        product.getImages().removeIf(img -> img.getImageUrl() == null || img.getImageUrl().isBlank());

        model.addAttribute("lang", locale.getLanguage());
        model.addAttribute("nameUk", product.getProductTranslation("uk").getName());
        model.addAttribute("descriptionUk", product.getProductTranslation("uk").getDescription());
        model.addAttribute("namePl", product.getProductTranslation("pl").getName());
        model.addAttribute("descriptionPl", product.getProductTranslation("pl").getDescription());
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
        @PathVariable Long id,
        Locale locale,
        @RequestParam (name = "nameUk") String nameUk,
        @RequestParam (name = "descriptionUk") String descriptionUk,
        @RequestParam (name = "namePl") String namePl,
        @RequestParam (name = "descriptionPl") String descriptionPl) throws IOException {

        Product existing = productService.findProductById(id);

        if(existing == null) {throw new RuntimeException("Product with id: " + id + " Does not exists.");}
                
        // ProductTranslation pt = existing.getProductTranslation(locale.getLanguage());

        // if(pt == null) {
        //     pt = new ProductTranslation();
        //     pt.setLanguageCode(locale.getLanguage());
        //     pt.setProduct(existing);
        //     existing.getTranslations().add(pt);
        // }

        // pt.setName(product.getProductTranslation(locale.getLanguage()).getName());
        // pt.setDescription(product.getProductTranslation(locale.getLanguage()).getDescription());
        
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());
        existing.setColor(product.getColor());
        existing.setMaterial(product.getMaterial());
        existing.setCategory(product.getCategory());
        
        Path uploadDir = Paths.get(MEDIA_PATH);

        Files.createDirectories(uploadDir);

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
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Добавляем в список
                Image image = new Image(existing, "/media/" + fileName);
                existing.getImages().add(image);
            }
        }

        productService.saveProduct(productService.updateProductSizesQuantity(existing, product.getSizes()));
        productService.setNewTranslationForProduct(existing, "uk", nameUk, descriptionUk);
        productService.setNewTranslationForProduct(existing, "pl", namePl, descriptionPl);
        return "redirect:/admin/products";
    }



    @DeleteMapping("/products/image/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.deleteImageById(id);
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

    @GetMapping("/orders/edit/{id}")
    public String getUpdateOrderPage(@PathVariable Long id, Model model) {
        Order order = orderService.findOrderById(id);
        List<Status> statuses = statusService.findAllStatuses();

        model.addAttribute("order", order);
        model.addAttribute("statuses", statuses);
        return "admin/updateOrder";
    }

    @PostMapping("/orders/edit")
    public String postUpdateOrderPage(@RequestParam("orderId") Long id, @RequestParam("statusId") Integer statusId, Model model) {
        Order orderFromDb = orderService.findOrderById(id);
        Status status = statusService.findStatusById(statusId);
        

        orderFromDb.setStatus(status);
        orderFromDb.setUpdatedAt(LocalDateTime.now());
        orderService.saveOrder(orderFromDb);

        confirmationEmailService.sendOrderInfoEmail(status, orderFromDb);

        return "redirect:/admin/orders";
    }





    @GetMapping("/categories")
    public String getAdminPanelCategories(Model model, HttpServletRequest request) {
        model.addAttribute("category", new Category());
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("categories", productService.countProductsByCategory());
        return "admin/adminPanelCategories";
    }

    @PostMapping("/categories/add")
    public String postAdminPanelCategoriesAdd(Model model, @ModelAttribute Category category) {
        if(categoryService.existsByName(category.getNameUa(), category.getNamePl())) {
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
    public String getAdminPanelColors(Model model, HttpServletRequest request, Locale locale) {
        model.addAttribute("lang", locale.getLanguage());
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("newColor", new Color());
        model.addAttribute("colors", productService.countProductsByColor());
        return "admin/adminPanelColors";
    }

    @PostMapping("/colors/add")
    public String postAdminPanelColorsAdd(Model model, @ModelAttribute Color color) {
        if(colorService.existsByName(color.getNameUa(), color.getNamePl())) {
            model.addAttribute("error", "Color with this name already exists");
            return "redirect:/admin/colors";
        }

        colorService.saveColor(color);
        return "redirect:/admin/colors";
    }



    @GetMapping("/materials")
    public String getAdminPageMaterials(Model model, HttpServletRequest request, Locale locale) {

        model.addAttribute("lang", locale.getLanguage());
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("newMaterial", new Material());
        model.addAttribute("materials", productService.countProductsByMaterial());
        return "admin/adminPanelMaterials";
    }

    @PostMapping("/materials/add")
    public String postAdminPageMaterialAdd(Model model, @ModelAttribute Material material) {
        if(materialService.existsByName(material.getNameUa(), material.getNamePl())) {
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
