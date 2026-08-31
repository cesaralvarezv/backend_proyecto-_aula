package com.backend_yireh_store.domain.port.out;

import com.backend_yireh_store.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    List<Product> findByActiveTrue();
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);
    List<Product> searchByName(String query);
    List<Product> searchByNameAndCategory(String query, Long categoryId);
    boolean existsByNameIgnoreCase(String name);
}