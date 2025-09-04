package com.bshop_jpa.demo.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.User;
import com.bshop_jpa.demo.repositories.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> findRecentUsers(int limit) {
        return userRepo.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit));
    }

}
