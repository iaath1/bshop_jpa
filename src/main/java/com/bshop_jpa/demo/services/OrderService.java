package com.bshop_jpa.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import com.bshop_jpa.demo.repositories.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.DTO.StatusCountDTO;
import com.bshop_jpa.demo.models.Order;
import com.bshop_jpa.demo.repositories.OrderRepository;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo, ProductRepository productRepository) {
        this.orderRepo = orderRepo;
        this.productRepository = productRepository;
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
        return orderRepo.findByPaidFalseAndCreatedAtBefore(dateTime);
    }

    public void deleteAllOrders(List<Order> orders) {
        orderRepo.deleteAll(orders);
    }


}
