package com.bshop_jpa.demo.services;

import com.bshop_jpa.demo.repositories.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.bshop_jpa.demo.repositories.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.DTO.StatusCountDTO;
import com.bshop_jpa.demo.models.Order;
import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.repositories.OrderRepository;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo, ProductRepository productRepository, UserService userService, UserRepository userRepository) {
        this.orderRepo = orderRepo;
        this.productRepository = productRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public List<Order> findRecentOrders(int limit) {
        return orderRepo.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit));
    }

    public void saveOrder(Order order) {
        orderRepo.save(order);
    }

    public List<Order> findAllOrders() {
        return orderRepo.findAll();
    }

    public List<StatusCountDTO> countOrdersByStatus() {
        return orderRepo.countOrdersByStatus();
    }

    public Long countOrders() {
        return orderRepo.count();
    }

    public Order findOrderById(Long orderId) {
        return orderRepo.findById(orderId).orElse(null);
    }

    public List<Order> findUnpaidAndExpired(LocalDateTime dateTime) {
        List<Order> orders = orderRepo.findAll();

        return orders.stream().filter(order -> order.getStatus().getName().equals("NEW") && order.getCreatedAt().isBefore(dateTime)).toList();
    }

    public void deleteAllOrders(List<Order> orders) {
        orderRepo.deleteAll(orders);
    }

    public List<Order> getOrdersByUser(User user) {
        List<Order> allOrders = orderRepo.findAll();
        List<Order> userOrders = new ArrayList<>();

        for(Order o : allOrders) {
            if(o.getUser() != null && o.getUser().getId() == user.getId()) {
                userOrders.add(o);
            }
        }

        return userOrders;
    }


}
