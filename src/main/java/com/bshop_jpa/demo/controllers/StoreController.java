package com.bshop_jpa.demo.controllers;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bshop_jpa.demo.models.Cart;
import com.bshop_jpa.demo.models.CartItem;
import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.models.Size;
import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.services.CartItemService;
import com.bshop_jpa.demo.services.CartService;
import com.bshop_jpa.demo.services.CategoryService;
import com.bshop_jpa.demo.services.ColorService;
import com.bshop_jpa.demo.services.MaterialService;
import com.bshop_jpa.demo.services.ProductService;
import com.bshop_jpa.demo.services.SizeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/store")
public class StoreController {

    private final ProductService productService;
    private final CartItemService cartItemService;
    private final CartService cartService;
    private final SizeService sizeService;
    private final ColorService colorService;
    private final CategoryService categoryService;
    private final MaterialService materialService;

    public StoreController(ProductService productService, CartService cartService, CartItemService cartItemService,
         SizeService sizeService, ColorService colorService, CategoryService categoryService, MaterialService materialService)  {

        this.cartService = cartService;
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.sizeService = sizeService;
        this.colorService = colorService;
        this.categoryService = categoryService;
        this.materialService = materialService;
    }

    @GetMapping
    public String getStore(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Integer colorId,
        @RequestParam(defaultValue = "name") String sortBy,
        @RequestParam(defaultValue = "asc") String order,
        @RequestParam(required = false) Integer categoryId,
        @RequestParam(required = false) String sizeName,
        @RequestParam(required = false) Integer materialId,
        HttpServletRequest request,
        Model model,
        Locale locale
    ) {

        //везде добавить
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
        model.addAttribute("colors", colorService.findAllColors());
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("products",  products);
        model.addAttribute("sizes", sizeService.getBlankSizes());
        model.addAttribute("materials", materialService.findAllMaterials());
        model.addAttribute("parameters", params);
        return "store/store";
    }

    // @GetMapping("/category/{id}")
    // public String getStoreCategoryDetails(@PathVariable Integer id, Model model, HttpServletRequest request) {
    //     Category category = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException());

    //     model.addAttribute("currentUrl", request.getRequestURI());
    //     model.addAttribute("category", category);
    //     model.addAttribute("products", productService.findProductsByCategory(category));
    //     return "store/categoryDetails";
    // }

    @GetMapping("/product/{id}")
    public String getStoreProductDetails(@PathVariable Long id, Model model, HttpServletRequest request) {
        Product product = productService.findProductById(id);
        if(product == null) {
            return "redirect:/store";
        }

        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("product", productService.sortProductSizes(product));
        return "store/productDetails";
    }

    @GetMapping("/cart")
    public String getCart(Model model, @AuthenticationPrincipal User user, HttpServletRequest request) {
        Cart cart = cartService.findCartByUser(user);

        BigDecimal total = BigDecimal.ZERO;


        if(!cart.getItems().isEmpty()) {
            for(CartItem item : cart.getItems()) {
                total = total.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }

        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("total", total);
        return "store/cart";
    }


    @PostMapping("/cart/add/{id}")
    public String cartAddProduct(@PathVariable Long id, @AuthenticationPrincipal User user, @RequestParam(required = true) Integer sizeId) {
        Product product = productService.findProductById(id);
        Cart cart = cartService.findCartByUser(user);
        Size size = sizeService.findSizeById(sizeId);

        if(product == null) {
            return "redirect:/store";
        }

        List<CartItem> cartItems = cart.getItems();

        for(CartItem item : cartItems) {
            if(item.getProduct().getId() == id) {
                return "redirect:/store";
            };
        }

        cartService.addProductToCart(cart, product, size);
        return "redirect:/store/cart";

    }

    @PostMapping("/cart/update")
    public String cartUpdateProduct(@RequestParam Long cartItemId, @RequestParam Integer quantity, @RequestParam Integer sizeId, Model model) {
        if(sizeService.findSizeById(sizeId).getQuantity() < quantity) {
            model.addAttribute("error", "You cant order more products than exists.");
            return "redirect:/store/cart";
        }

        CartItem cartItemFromDb = cartItemService.findCartItemById(cartItemId);
        if(cartItemFromDb == null) {
            return "redirect:/store";
        }

        cartItemFromDb.setQuantity(quantity);
        cartItemService.saveCartItem(cartItemFromDb);
        return "redirect:/store/cart";
    }

    @PostMapping("/cart/delete/{id}")
    public String cartDeleteProduct(@PathVariable Long id) {
        cartItemService.deleteCartItemById(id);

        return "redirect:/store/cart";
    }

}
