package com.bshop_jpa.demo.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.Order;

@Service
public class OrderCleanupService {
    private final OrderService orderService;

    public OrderCleanupService(OrderService orderService) {
        this.orderService = orderService;
    }

    //5 min
    @Scheduled(fixedRate = 300000)
    public void cleanup() {

        // Change to 30 minutes in deployment
        LocalDateTime limit = LocalDateTime.now().minusMinutes(5);

        List<Order> expiredOrders = orderService.findUnpaidAndExpired(limit);

        orderService.deleteAllOrders(expiredOrders);

    }
}
