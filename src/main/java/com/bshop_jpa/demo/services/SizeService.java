package com.bshop_jpa.demo.services;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.Size;
import com.bshop_jpa.demo.repositories.SizeRepository;

@Service
public class SizeService {

    private final SizeRepository sizeRepo;

    public SizeService(SizeRepository sizeRepo) {
        this.sizeRepo = sizeRepo;
    }

    public Size getSizeById(Integer id) {
        if(sizeRepo.findById(id).isPresent()) {
            return sizeRepo.findById(id).get();
        }

        return null;
    }

    public Size findSizeById(Integer id) {
        if(sizeRepo.findById(id).isPresent()) {
            return sizeRepo.findById(id).get();
        }

        return null;
    }

    public void saveSize(Size size) {
        sizeRepo.save(size);
    }
    
}
