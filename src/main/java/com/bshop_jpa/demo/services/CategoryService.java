package com.bshop_jpa.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.repositories.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> findAllCategories() {
        return categoryRepo.findAll();
    }
}
