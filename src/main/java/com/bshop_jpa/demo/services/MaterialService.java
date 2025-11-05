package com.bshop_jpa.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.Material;
import com.bshop_jpa.demo.repositories.MaterialRepository;

@Service
public class MaterialService {

    private final MaterialRepository materialRepo;

    public MaterialService(MaterialRepository materialRepo) {
        this.materialRepo = materialRepo;
    }

    public List<Material> findAllMaterials() {
        return materialRepo.findAll();
    }

    public boolean existsByName(String name) {
        return materialRepo.existsByName(name);
    }

    public void saveMaterial(Material material) {
        materialRepo.save(material);
    }
    
}
