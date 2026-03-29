package com.bshop_jpa.demo.services;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

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

    @Transient
    public List<Size> getBlankSizes() {
        List<Size> sizes = new ArrayList<>();

        Size xs = new Size("XS");
        Size s = new Size("S");
        Size m =  new Size("M");
        Size l =  new Size("L");
        Size xl =  new Size("XL");

        sizes.add(xs);
        sizes.add(s);
        sizes.add(m);
        sizes.add(l);
        sizes.add(xl);

        return sizes;
    }
    
}
