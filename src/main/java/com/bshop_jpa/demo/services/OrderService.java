package com.bshop_jpa.demo.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
}
