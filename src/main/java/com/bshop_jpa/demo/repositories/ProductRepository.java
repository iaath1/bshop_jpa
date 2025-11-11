package com.bshop_jpa.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bshop_jpa.demo.DTO.CategoryCountDTO;
import com.bshop_jpa.demo.DTO.ColorCountDTO;
import com.bshop_jpa.demo.DTO.MaterialCountDTO;
import com.bshop_jpa.demo.models.Category;
import com.bshop_jpa.demo.models.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    
    @Query("SELECT new com.bshop_jpa.demo.DTO.CategoryCountDTO(c.nameUa, c.namePl, c.id, COUNT(p)) " +
       "FROM Category c LEFT JOIN Product p ON p.category = c " +
       "GROUP BY c.nameUa, c.namePl, c.id")
    List<CategoryCountDTO> countProductsByCategory();

   //  @Query("SELECT new com.bshop_jpa.demo.DTO.SizeCountDTO(s.name, s.id, COUNT(p)) " +
   //     "FROM Size s LEFT JOIN Product p ON p.size = s " +
   //     "GROUP BY s.name, s.id")
   //  List<SizeCountDTO> countProductsBySize();

    @Query("SELECT new com.bshop_jpa.demo.DTO.ColorCountDTO(c.name, COUNT(p), c.hexValue) " +
       "FROM Color c LEFT JOIN Product p ON p.color = c " +
       "GROUP BY c.name, c.hexValue")
    List<ColorCountDTO> countProductsByColor();

    @Query("SELECT new com.bshop_jpa.demo.DTO.MaterialCountDTO(m.name, COUNT(p)) " +
       "FROM Material m LEFT JOIN Product p ON p.material = m " +
       "GROUP BY m.name")
    List<MaterialCountDTO> countProductsByMaterial();

   //  @Query("SELECT DISTINCT p FROM Product p " + 
   //          "LEFT JOIN FETCH p.translations t" +
   //          "WHERE t.languageCode = :lang")
   //  List<Product> findAllWithTranslations(@Param("lang") String lang);

    List<Product> findByCategory(Category category);

    List<Product> findAll();

    List<Product> findByNameContainingIgnoreCase(String name);
}
