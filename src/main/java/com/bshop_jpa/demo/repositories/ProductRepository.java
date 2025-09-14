package com.bshop_jpa.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bshop_jpa.demo.DTO.CategoryCountDTO;
import com.bshop_jpa.demo.DTO.ColorCountDTO;
import com.bshop_jpa.demo.DTO.MaterialCountDTO;
import com.bshop_jpa.demo.DTO.SizeCountDTO;
import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.models.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    
    @Query("SELECT new com.bshop_jpa.demo.DTO.CategoryCountDTO(c.name, c.id, COUNT(p)) " +
       "FROM Category c LEFT JOIN Product p ON p.category = c " +
       "GROUP BY c.name, c.id")
    List<CategoryCountDTO> countProductsByCategory();

    @Query("SELECT new com.bshop_jpa.demo.DTO.SizeCountDTO(s.name, s.id, COUNT(p)) " +
       "FROM Size s LEFT JOIN Product p ON p.size = s " +
       "GROUP BY s.name, s.id")
    List<SizeCountDTO> countProductsBySize();

    @Query("SELECT new com.bshop_jpa.demo.DTO.ColorCountDTO(c.name, COUNT(p)) " +
       "FROM Color c LEFT JOIN Product p ON p.color = c " +
       "GROUP BY c.name")
    List<ColorCountDTO> countProductsByColor();

    @Query("SELECT new com.bshop_jpa.demo.DTO.MaterialCountDTO(m.name, COUNT(p)) " +
       "FROM Material m LEFT JOIN Product p ON p.material = m " +
       "GROUP BY m.name")
    List<MaterialCountDTO> countProductsByMaterial();

    List<Product> findByCategory(Category category);
}
