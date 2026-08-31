package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.exception.CategoryAlreadyExistsException;
import com.backend_yireh_store.domain.exception.CategoryHasProductsException;
import com.backend_yireh_store.domain.exception.CategoryNotFoundException;
import com.backend_yireh_store.domain.model.Category;
import com.backend_yireh_store.domain.port.in.CategoryUseCase;
import com.backend_yireh_store.domain.port.out.CategoryRepositoryPort;
import com.backend_yireh_store.domain.port.out.ProductRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService implements CategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;
    private final ProductRepositoryPort productRepository;

    public CategoryService(CategoryRepositoryPort categoryRepository,
                           ProductRepositoryPort productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Category create(String name, String description, String icon, int order) {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new CategoryAlreadyExistsException(name);
        }
        Category category = Category.createNew(name, description, icon, order);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getActive() {
        return categoryRepository.findByActiveTrue();
    }

    @Override
    @Transactional
    public Category update(Long id, String name, String description, String icon, Integer order) {
        Category category = getById(id);
        if (name != null && !name.equalsIgnoreCase(category.getName())) {
            if (categoryRepository.existsByNameIgnoreCase(name)) {
                throw new CategoryAlreadyExistsException(name);
            }
        }
        category.update(name, description, icon, order);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category deactivate(Long id) {
        Category category = getById(id);
        category.deactivate();
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category activate(Long id) {
        Category category = getById(id);
        category.activate();
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = getById(id);
        List<?> products = productRepository.findByCategoryId(id);
        if (!products.isEmpty()) {
            throw new CategoryHasProductsException(category.getName());
        }
        categoryRepository.deleteById(id);
    }
}
