package com.bshop_jpa.demo.services;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.CartItem;
import com.bshop_jpa.demo.repositories.CartItemRepository;

@Service
public class CartItemService {
    
    private final CartItemRepository cartItemRepo;

    public CartItemService(CartItemRepository cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    public void saveCartItem(CartItem cartItem) {
        cartItemRepo.save(cartItem);
    }

    public CartItem findCartItemById(Long id) {
        if(!cartItemRepo.existsById(id)) {
            return null;
        }

        return cartItemRepo.findById(id).get();
    }

    public void deleteCartItemById(Long id) {
        cartItemRepo.deleteById(id);
    }

    // public List<CartItem> findCartItemsByCart(Cart cart) {
    //     cartItemRepo.findAllByCart(Cart cart);
    // }

}
