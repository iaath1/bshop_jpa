package com.bshop_jpa.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bshop_jpa.demo.models.ProductTranslation;

@Repository
public interface ProductTranslationRepository extends JpaRepository<ProductTranslation, Integer> {
    
}
