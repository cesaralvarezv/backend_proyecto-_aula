package com.backend_yireh_store.domain.port.out;

import com.backend_yireh_store.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort {
    Category save(Category category);
    Optional<Category> findById(Long id);
    Optional<Category> findByNameIgnoreCase(String name);
    List<Category> findAll();
    List<Category> findByActiveTrue();
    boolean existsByNameIgnoreCase(String name);
    void deleteById(Long id);
}
