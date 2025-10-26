package com.bshop_jpa.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.Color;
import com.bshop_jpa.demo.repositories.ColorRepository;

@Service
public class ColorService {

    private final ColorRepository colorRepo;

    public ColorService(ColorRepository colorRepo) {
        this.colorRepo = colorRepo;
    }

    public List<Color> findAllColors() {
        return colorRepo.findAll();
    }
    
}
