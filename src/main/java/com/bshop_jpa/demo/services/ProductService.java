package com.bshop_jpa.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.DTO.CategoryCountDTO;
import com.bshop_jpa.demo.DTO.ColorCountDTO;
import com.bshop_jpa.demo.DTO.MaterialCountDTO;
import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.models.Color;
import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.models.Size;
import com.bshop_jpa.demo.repositories.ProductRepository;

@Service
public class ProductService {
    
    private final ProductRepository productRepo;
    private final SizeService sizeService;

    public ProductService(ProductRepository productRepo, SizeService sizeService) {
        this.productRepo = productRepo;
        this.sizeService = sizeService;
    }

    public Product findProductById(Long id) {

        if(productRepo.existsById(id)) {
            return productRepo.findById(id).get();
        }
        return null;
    }

    public Iterable<Product> findAllProducts() {
        return productRepo.findAll();
    }

    public List<CategoryCountDTO> countProductsByCategory() {
        return productRepo.countProductsByCategory();
    }

    public List<Product> findProductsByCategory(Category category) {
        return productRepo.findByCategory(category);
    }

    public long countAllProduct() {
        return productRepo.count();
    }

    public Product updateProductSizesQuantity(Product product, List<Size> updatedSizes) {
    if (product.getSizes() == null || updatedSizes == null) {
        return product;
    }

    for (Size updatedSize : updatedSizes) {
        product.getSizes().stream()
                .filter(existingSize -> existingSize.getId().equals(updatedSize.getId()))
                .findFirst()
                .ifPresent(existingSize -> existingSize.setQuantity(updatedSize.getQuantity()));
    }

    return product;
}

    public void saveProduct(Product product) {
        productRepo.save(product);
    }

    public void deleteProductById(Long id) {
        productRepo.deleteById(id);
    }

    public List<ColorCountDTO> countProductsByColor() {
        return productRepo.countProductsByColor();
    }

    public List<MaterialCountDTO> countProductsByMaterial() {
        return productRepo.countProductsByMaterial();
    }

    public List<Size> setBlankSizes(Product product) {
        List<Size> sizes = new ArrayList<>();

        Size xs = new Size("XS", 0, product);
        Size s = new Size("S", 0, product);
        Size m =  new Size("M", 0, product);
        Size l =  new Size("L", 0, product);
        Size xl =  new Size("XL", 0, product);

        sizes.add(xs);
        sizes.add(s);
        sizes.add(m);
        sizes.add(l);
        sizes.add(xl);

        return sizes;
    }

    public void saveNewProduct(Product product) {
        if(product.getSizes() != null) {
            for(Size size : product.getSizes()) {
                size.setProduct(product);
            }
        }

        productRepo.save(product);
    }

    

}
