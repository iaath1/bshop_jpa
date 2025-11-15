package com.bshop_jpa.demo.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bshop_jpa.demo.models.CartItem;
import com.bshop_jpa.demo.models.Order;
import com.bshop_jpa.demo.models.OrderItem;
import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.models.ProductForOrder;
import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.repositories.SizeRepository;
import com.bshop_jpa.demo.services.CartService;
import com.bshop_jpa.demo.services.OrderService;
import com.bshop_jpa.demo.services.ProductService;
import com.bshop_jpa.demo.services.SizeService;
import com.bshop_jpa.demo.services.StatusService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/order")
public class OrderController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CartService cartService;
    private final StatusService statusService;
    private final SizeService sizeService;

    public OrderController(ProductService productService, OrderService orderService, CartService cartService, StatusService statusService, SizeService sizeService) {
        this.productService = productService;
        this.orderService = orderService;
        this.cartService = cartService;
        this.statusService = statusService;
        this.sizeService = sizeService;
    }

    @GetMapping("/cart")
    public String getOrderPage(Model model, @AuthenticationPrincipal User user, HttpServletRequest request, Locale locale) {

        if(user == null) {
            return "redirect:/login";
        }

        BigDecimal total = BigDecimal.ZERO;
        List<CartItem> cartItems = cartService.findCartByUser(user).getItems();


        for(CartItem item : cartItems) {
            total = total.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.getProduct().setName(
                productService.localizateProduct(item.getProduct(), locale.getLanguage()).getName()
            );
        }

        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);

        //надо улучшить, считаются только разные товары не учитывая количество каждого
        model.addAttribute("totalQuantity", cartService.findCartByUser(user).getItems().size());
        model.addAttribute("totalAmount", total);
        return "order/order";
    }



    
    @GetMapping("/{id}")
    public String getOrderPageUnauthorized(Model model, @PathVariable Long id, @AuthenticationPrincipal User user, HttpServletRequest request,
     @RequestParam(required = true, name = "sizeId") Integer sizeId, Locale locale) {

        Product product = productService.findProductById(id);

        if(product == null) {
            return "redirect:/store";
        }

        if(user != null) {
            model.addAttribute("user", user);
        }

        ProductForOrder productForOrder = new ProductForOrder();
        productForOrder.setName(productService.localizateProduct(product, locale.getLanguage()).getName());
        productForOrder.setPrice(product.getPrice());
        productForOrder.setProductId(id);
        productForOrder.setQuantity(1);
        productForOrder.setSize(sizeService.getSizeById(sizeId));
        productForOrder.setColor(product.getColor());
        productForOrder.setMaterial(product.getMaterial());
        productForOrder.setPreviewImageUrl(product.getPreviewImageUrl());

        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("productForOrder", productForOrder);
        return "order/order";
    }

    @GetMapping("/address")
    public String getOrderAddressPage() {
        return "order/address";
    }

    @PostMapping("/address/submit")
    public String postOrderAddressPage(@AuthenticationPrincipal User user,
                                        @RequestParam String lockerId,
                                        @RequestParam String lockerName,
                                        @RequestParam String lockerAddress) {
        Order order = new Order();

        if(user != null) {
            order.setUser(user);
            
        }

        List<CartItem> cartItems = cartService.findCartByUser(user).getItems();
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for(CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setPrice(item.getProduct().getPrice());
            orderItem.setQuantity(item.getQuantity());
            
            total = total.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            orderItems.add(orderItem);
        }

        order.setLockerAddress(lockerAddress);
        order.setLockerId(lockerId);
        order.setLockerName(lockerName);
        order.setTotalPrice(total);
        order.setItems(orderItems);
        order.setStatus(statusService.findStatusByName("NEW"));

        orderService.saveOrder(order);
        cartService.clearCartByUser(user);
        
        return "redirect:/payment";
    }


    @GetMapping("/address/unauthorized")
    public String getOrderAddresUnathorized(Model model, @RequestParam(required = true, name = "productForOrder.sizeId") Integer sizeId,
                                            @RequestParam(required = true, name = "productForOrder.productId") Long productId,
                                            @RequestParam(required = true, name = "productForOrder.quantity") Integer quantity,
                                            @RequestParam(required = false) String email) {

        model.addAttribute("sizeId", sizeId);
        model.addAttribute("productId", productId);
        model.addAttribute("quantity", quantity);
        model.addAttribute("email", email);
        return "order/address";
    }


    @PostMapping("/address/unauthorized/submit")
    public String postOrderPageUnathorized(@RequestParam String lockerId,
                                            @RequestParam String lockerName,
                                            @RequestParam String lockerAddress,
                                            @RequestParam Long productId,
                                            @RequestParam Integer sizeId,
                                            @RequestParam Integer quantity,
                                            @RequestParam String email) {
        Order order = new Order();

        order.setGuestEmail(email);
        order.setLockerAddress(lockerAddress);
        order.setLockerId(lockerId);
        order.setLockerName(lockerName);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        Product productForOrder = productService.findProductById(productId);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(productService.findProductById(productId));
        orderItem.setPrice(productForOrder.getPrice());
        orderItem.setQuantity(quantity);
        
        total = total.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        orderItems.add(orderItem);

        order.setTotalPrice(total);
        order.setItems(orderItems);
        order.setStatus(statusService.findStatusByName("NEW"));

        orderService.saveOrder(order);
        
        return "redirect:/payment";
    }


    
}
