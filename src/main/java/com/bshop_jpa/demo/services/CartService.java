package com.bshop_jpa.demo.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.Cart;
import com.bshop_jpa.demo.models.CartItem;
import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.repositories.CartRepository;

@Service
public class CartService {

    private final CartItemService cartItemService;
    
    private final CartRepository cartRepo;

    public CartService(CartRepository cartRepo, CartItemService cartItemService) {
        this.cartRepo = cartRepo;
        this.cartItemService = cartItemService;
    }

    public Cart findCartById(Long id) {
        if(!cartRepo.existsById(id)) {
            return null;
        }

        return cartRepo.findById(id).get();
    }

    public Cart findCartByUser(User user) {

        if(!cartRepo.findByUser(user).isPresent()) {
            Cart cart = new Cart(user, new ArrayList<>());
            cartRepo.save(cart);
        }

        return cartRepo.findByUser(user).get();
    }

    public void saveCart(Cart cart) {
        cartRepo.save(cart);
    }

    public void addProductToCart(Cart cart, Product product) {
        CartItem cartItem = new CartItem(cart, product, 1);
        cart.getItems().add(cartItem);
        cartItemService.saveCartItem(cartItem);
        cartRepo.save(cart);
    }

}
