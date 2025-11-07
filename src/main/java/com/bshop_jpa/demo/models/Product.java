package com.bshop_jpa.demo.models;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = true, length = 100)
    private String name;

    @Column(nullable = true, length = 200)
    private String description;

    @OneToMany(mappedBy = "product", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ProductTranslation> translations = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = true)
    private Integer quantity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Size> sizes;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    @Column(name = "images_url", length = 255)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    
    public ProductTranslation getProductTranslation(String locale) {
        return translations.stream()
            .filter(t -> t.getLanguageCode().equalsIgnoreCase(locale))
            .findFirst()
            .orElse(null);
    }

    @Transient
    public String getPreviewImageUrl() {
        if (images != null && !images.isEmpty()) {
            return images.get(0).getImageUrl();
        }
        return "/uploads/products/no-image.png"; // путь к дефолтной картинке
    }

}
