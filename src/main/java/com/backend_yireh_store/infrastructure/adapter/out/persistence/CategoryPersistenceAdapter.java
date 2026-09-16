package com.backend_yireh_store.infrastructure.adapter.out.persistence;

import com.backend_yireh_store.domain.model.Category;
import com.backend_yireh_store.domain.port.out.CategoryRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.repository.SpringCategoryJpaRepository;
import com.backend_yireh_store.infrastructure.mapper.CategoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private final SpringCategoryJpaRepository jpaRepository;
    private final CategoryMapper mapper;

    public CategoryPersistenceAdapter(SpringCategoryJpaRepository jpaRepository,
                                      CategoryMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = mapper.toEntity(category);
        CategoryEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Category> findByNameIgnoreCase(String name) {
        return jpaRepository.findByNameIgnoreCase(name).map(mapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Category> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Category> findByActiveTrueAndLocation(String location) {
        return jpaRepository.findByActiveTrueAndLocationIgnoreCase(location).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}