package com.bshop_jpa.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.DTO.CategoryCountDTO;
import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.repositories.ProductRepository;

@Service
public class ProductService {
    
    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public Product findProductById(Long id) {

        if(productRepo.existsById(id)) {
            return productRepo.findById(id).get();
        }
        return null;
    }

    public Iterable<Product> findAllProducts() {
        return productRepo.findAll();
    }

    public List<CategoryCountDTO> countProductsByCategory() {
        return productRepo.countProductsByCategory();
    }

    public List<Product> findProductsByCategory(Category category) {
        return productRepo.findByCategory(category);
    }


}
