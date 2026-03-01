package com.bshop_jpa.demo.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.Image;
import com.bshop_jpa.demo.repositories.ImageRepository;

import jakarta.transaction.Transactional;

@Service
public class ImageService {
    private final ImageRepository imageRepo;

    @Value("${app.delete.dir}")
    private String MEDIA_PATH;

    public ImageService(ImageRepository imageRepo) {
        this.imageRepo = imageRepo;
    }

    @Transactional
    public void deleteImageById(Long id) {

        Image image = null;

        if(imageRepo.findById(id).isPresent()) {
            image = imageRepo.findById(id).get();
        } else {
            throw new RuntimeException("Image with id: " + id + " was not found");
        }

        deleteFile(image.getImageUrl());
        imageRepo.delete(image);

        
    }

    private void deleteFile(String imageUrl) {
        try {
            Path path = Paths.get(MEDIA_PATH + imageUrl);
            System.out.println(path.toString());
            Files.deleteIfExists(path);
            System.out.println("File deleted: " + path.toString());
        } catch(IOException e) {
            throw new RuntimeException("Failed to delete image file", e);
        }
    }

}
