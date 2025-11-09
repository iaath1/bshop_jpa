package com.bshop_jpa.demo.services;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.ProductTranslation;
import com.bshop_jpa.demo.repositories.ProductTranslationRepository;

@Service
public class ProductTranslationService {
    private final ProductTranslationRepository ptRepo;

    public ProductTranslationService(ProductTranslationRepository ptRepo) {
        this.ptRepo = ptRepo;
    }

    public void saveTranslation(ProductTranslation pt) {
        ptRepo.save(pt);
    }
}
