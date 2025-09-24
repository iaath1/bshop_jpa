package com.bshop_jpa.demo.services;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.Status;
import com.bshop_jpa.demo.repositories.StatusRepository;

@Service
public class StatusService {
    
    private final StatusRepository statusRepo;

    public StatusService(StatusRepository statusRepo) {
        this.statusRepo = statusRepo;
    }

    public Status findStatusByName(String name) {
        if(statusRepo.findByName(name).isPresent()) {
            return statusRepo.findByName(name).get();
        }

        return null;
    }

}
