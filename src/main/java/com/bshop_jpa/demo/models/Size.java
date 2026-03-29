package com.bshop_jpa.demo.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "sizes")
@Data
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 4, unique = false)
    private String name;

    @Column(nullable = false)
    public Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Size() {}

    public Size(String name, Integer quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Size(String name) {
        this.name = name;
    }
}