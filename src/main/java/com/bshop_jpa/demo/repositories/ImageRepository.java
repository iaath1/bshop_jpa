package com.bshop_jpa.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bshop_jpa.demo.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
}
