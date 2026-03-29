package com.bshop_jpa.demo.models;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductForOrder {

    private Long productId;
    private String name;
    private Size size;
    private Integer quantity;
    private BigDecimal price;
    private Color color;
    private Material material;
    private String previewImageUrl;

    public ProductForOrder(Long productId, String name, Size size, Integer quantity, BigDecimal price, Color color, Material material, String previewImageUrl) {
        this.productId = productId;
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.color = color;
        this.material = material;
        this.previewImageUrl = previewImageUrl;
    }

    public ProductForOrder() {}
    
}
