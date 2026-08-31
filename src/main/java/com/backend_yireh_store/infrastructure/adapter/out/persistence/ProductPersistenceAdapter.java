package com.backend_yireh_store.infrastructure.adapter.out.persistence;

import com.backend_yireh_store.domain.model.Category;
import com.backend_yireh_store.domain.model.Product;
import com.backend_yireh_store.domain.port.out.CategoryRepositoryPort;
import com.backend_yireh_store.domain.port.out.ProductRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.repository.SpringProductJpaRepository;
import com.backend_yireh_store.infrastructure.mapper.ProductMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final SpringProductJpaRepository jpaRepository;
    private final CategoryRepositoryPort categoryRepository;
    private final ProductMapper mapper;

    public ProductPersistenceAdapter(SpringProductJpaRepository jpaRepository,
                                     CategoryRepositoryPort categoryRepository,
                                     ProductMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Product> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return jpaRepository.findByCategoryId(categoryId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Product> findByCategoryIdAndActiveTrue(Long categoryId) {
        return jpaRepository.findByCategoryIdAndActiveTrue(categoryId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Product> searchByName(String query) {
        return jpaRepository.searchByName(query).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Product> searchByNameAndCategory(String query, Long categoryId) {
        return jpaRepository.searchByNameAndCategory(query, categoryId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }
}