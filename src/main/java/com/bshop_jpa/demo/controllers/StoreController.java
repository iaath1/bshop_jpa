package com.bshop_jpa.demo.controllers;


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
import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.repositories.ProductRepository;
import com.bshop_jpa.demo.repositories.CategoryRepository;
import com.bshop_jpa.demo.services.CartItemService;
import com.bshop_jpa.demo.services.CartService;
import com.bshop_jpa.demo.services.ProductService;

@Controller
@RequestMapping("/store")
public class StoreController {
    private final ProductRepository productRepository;


    private final ProductService productService;
    private final CategoryRepository categoryRepo;
    private final CartService cartService;
    private final CartItemService cartItemService;
    public StoreController(ProductService productService, CategoryRepository categoryRepo, CartService cartService, CartItemService cartItemService, ProductRepository productRepository)  {
        this.productService = productService;
        this.categoryRepo = categoryRepo;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public String getStore(Model model) {
        model.addAttribute("categories", productService.countProductsByCategory());
        model.addAttribute("products", productService.findAllProducts());
        return "store/store";
    }

    @GetMapping("/category/{id}")
    public String getStoreCategoryDetails(@PathVariable Integer id, Model model) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException());
        model.addAttribute("category", category);
        model.addAttribute("products", productService.findProductsByCategory(category));
        return "store/categoryDetails";
    }

    @GetMapping("/product/{id}")
    public String getStoreProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.findProductById(id);
        if(product == null) {
            return "redirect:/";
        }

        model.addAttribute("product", product);
        return "store/productDetails";
    }

    @GetMapping("/cart")
    public String getCart(Model model, @AuthenticationPrincipal User user) {
        Cart cart = cartService.findCartByUser(user);

        model.addAttribute("cartItems", cart.getItems());
        return "store/cart";
    }


    @PostMapping("/cart/add/{id}")
    public String cartAddProduct(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Product product = productService.findProductById(id);
        Cart cart = cartService.findCartByUser(user);

        if(product == null) {
            return "redirect:/";
        }

        cartService.addProductToCart(cart, product);
        return "redirect:/store/cart";

    }

    @PostMapping("/cart/update")
    public String cartUpdateProduct(@RequestParam Long cartItemId, @RequestParam Integer quantity, @RequestParam Long productId, Model model) {
        if(productService.findProductById(productId).getQuantity() < quantity) {
            model.addAttribute("error", "You cant order more products than exists.");
            return "redirect:/store/cart";
        }

        CartItem cartItemFromDb = cartItemService.findCartItemById(cartItemId);
        if(cartItemFromDb == null) {
            return "redirect:/";
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
