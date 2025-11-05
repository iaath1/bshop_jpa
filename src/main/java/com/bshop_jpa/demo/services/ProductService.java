package com.bshop_jpa.demo.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.DTO.CategoryCountDTO;
import com.bshop_jpa.demo.DTO.ColorCountDTO;
import com.bshop_jpa.demo.DTO.MaterialCountDTO;
import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.models.Size;
import com.bshop_jpa.demo.repositories.ProductRepository;

@Service
public class ProductService {
    
    private final ProductRepository productRepo;
    private final SizeService sizeService;
    private static final List<String> SIZE_ORDER = List.of("XS", "S", "M", "L", "XL");

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

    public List<Product> sortProductSizes(List<Product> products) {

        for(Product product : products) {
            List<Size> sizes = product.getSizes();
            sizes.sort(Comparator.comparingInt(size -> SIZE_ORDER.indexOf(size.getName())));
            product.setSizes(sizes);
        }

        return products;
    }

    public Product sortProductSizes(Product product) {
        List<Size> sizes = product.getSizes();
        sizes.sort(Comparator.comparingInt(size -> SIZE_ORDER.indexOf(size.getName())));
        product.setSizes(sizes);

        return product;
    }

    public List<Product> findAllProducts() {
        return sortProductSizes(productRepo.findAll());
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

     public List<Product> getFilteredProducts(String query, Integer categoryId, Integer colorId, Integer materialId, String sizeName, String sortOrder) {
        List<Product> products;

        if (query != null && !query.isBlank()) {
            products = productRepo.findByNameContainingIgnoreCase(query);
        } else {
            products = productRepo.findAll();
        }

        // фильтр по категории
        if (categoryId != null) {
            products = products.stream()
                    .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(categoryId))
                    .toList();
        }

        // фильтр по цвету
        if (colorId != null) {
            products = products.stream()
                    .filter(p -> p.getColor() != null && p.getColor().getId().equals(colorId))
                    .toList();
        }

        if(materialId != null) {
            products = products.stream()
                .filter(p -> p.getMaterial() != null && p.getMaterial().getId().equals(materialId))
                .toList();
        }

        // сортировка по цене
        if ("desc".equalsIgnoreCase(sortOrder)) {
            products = products.stream()
                    .sorted(Comparator.comparing(Product::getPrice).reversed())
                    .toList();
        } else {
            products = products.stream()
                    .sorted(Comparator.comparing(Product::getPrice))
                    .toList();
        }

        List<Product> filteredProducts = new ArrayList<>();

        // фильтр по размеру (OneToMany)
        if(sizeName != null && !sizeName.isEmpty()) {
            for(Product product : products) {
                for(Size size : product.getSizes()) {
                    if(size.getName().equals(sizeName) && size.getQuantity() > 0) {
                        filteredProducts.add(product);
                    }
                }
            }

            return sortProductSizes(filteredProducts);
        }

        return sortProductSizes(products);
    }

    

}
