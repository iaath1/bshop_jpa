package com.bshop_jpa.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    public Image() {};

    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Image(Product product, String imageUrl) {
        this.product = product;
        this.imageUrl = imageUrl;
    }
}
