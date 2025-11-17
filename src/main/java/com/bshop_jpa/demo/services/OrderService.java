package com.bshop_jpa.demo.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.DTO.StatusCountDTO;
import com.bshop_jpa.demo.models.Order;
import com.bshop_jpa.demo.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
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


}
