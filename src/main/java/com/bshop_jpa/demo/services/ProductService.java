package com.bshop_jpa.demo.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.DTO.CategoryCountDTO;
import com.bshop_jpa.demo.DTO.ColorCountDTO;
import com.bshop_jpa.demo.DTO.MaterialCountDTO;
import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.models.Image;
import com.bshop_jpa.demo.models.Product;
import com.bshop_jpa.demo.models.ProductTranslation;
import com.bshop_jpa.demo.models.Size;
import com.bshop_jpa.demo.repositories.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
    
    private final ImageService imageService;
    private final ProductRepository productRepo;
    private final SizeService sizeService;
    private final ProductTranslationService ptService;
    private static final List<String> SIZE_ORDER = List.of("XS", "S", "M", "L", "XL");

    public ProductService(ProductRepository productRepo, SizeService sizeService, ProductTranslationService ptService, ImageService imageService) {
        this.productRepo = productRepo;
        this.sizeService = sizeService;
        this.ptService = ptService;
        this.imageService = imageService;
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

    @Transactional
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

    @Transactional
    public void saveProduct(Product product) {
        productRepo.save(product);
    }

    @Transactional
    public void deleteProductById(Long id) {
        List<Image> images = findProductById(id).getImages();

        for(Image image : images) {
            imageService.deleteImageById(image.getId());
        }

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

    @Transactional
    public void saveNewProduct(Product product) {
        if(product.getSizes() != null) {
            for(Size size : product.getSizes()) {
                size.setProduct(product);
            }
        }

        productRepo.save(product);
    }

     public List<Product> getFilteredProducts(String query, Integer categoryId, Integer colorId, Integer materialId, String sizeName, String sortOrder, Locale locale) {
        List<Product> products;

        if (query != null && !query.isBlank()) {
            products = findProductByName(query, localizateProducts(productRepo.findAll(), locale.getLanguage()));
        } else {
            products = localizateProducts(productRepo.findAll(), locale.getLanguage());
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

    // public List<Product> findAllWIthTranslations(String lang) {
    //     return productRepo.findAllWithTranslations(lang);
    // }

    // public List<Product> findAllWithTranslation(String langCode) {
    //     return productRepo.findAllWithTranslations(langCode);
    // }

    public List<Product> localizateProducts(List<Product> products, String lang) {
        return products.stream()
            .map(p -> {
                ProductTranslation t = p.getProductTranslation(lang);
                if(t != null) {
                    p.setName(t.getName());
                    p.setDescription(t.getDescription());
                }

                return p;
            })
            .toList();
    }

    public Product localizateProduct(Product product, String lang) {
        ProductTranslation pt = product.getProductTranslation(lang);

        if(pt != null) {
            product.setName(pt.getName());
            product.setDescription(pt.getDescription());
        }

        return product;
    }

    public List<Product> findProductByName(String name, List<Product> products) {
        return products.stream()
            .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();
    }

    public void addTranslationToProduct(Product product, String language, String name, String description) {
        ProductTranslation pt = new ProductTranslation();
        pt.setLanguageCode(language);
        pt.setProduct(product);
        pt.setName(name);
        pt.setDescription(description);

        ptService.saveTranslation(pt);
    }
    
    public void setNewTranslationForProduct(Product product, String language, String name, String description) {
        product.getTranslations().removeIf(pt -> pt.getLanguageCode().equals(language));

        ProductTranslation pt = new ProductTranslation();
        pt.setLanguageCode(language);
        pt.setDescription(description);
        pt.setName(name);
        pt.setProduct(product);

        product.getTranslations().add(pt);
        productRepo.save(product);
        //ptService.saveTranslation(pt);
    }

}
