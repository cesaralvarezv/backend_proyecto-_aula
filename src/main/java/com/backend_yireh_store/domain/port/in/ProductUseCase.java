package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.Product;
import com.backend_yireh_store.domain.model.Category;

import java.math.BigDecimal;
import java.util.List;

public interface ProductUseCase {
    Product create(String name, BigDecimal price, String description, String imageUrl,
                   Long categoryId, int order);
    Product getById(Long id);
    List<Product> getAll();
    List<Product> getActive();
    List<Product> getByCategory(Long categoryId);
    List<Product> getActiveByCategory(Long categoryId);
    List<Product> searchByName(String query);
    List<Product> searchByNameAndCategory(String query, Long categoryId);
    Product update(Long id, String name, BigDecimal price, String description,
                   String imageUrl, Long categoryId, Integer order);
    Product deactivate(Long id);
    Product activate(Long id);
}