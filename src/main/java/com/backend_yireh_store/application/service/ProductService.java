package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.exception.CategoryNotFoundException;
import com.backend_yireh_store.domain.exception.ProductAlreadyExistsException;
import com.backend_yireh_store.domain.exception.ProductNotFoundException;
import com.backend_yireh_store.domain.model.Category;
import com.backend_yireh_store.domain.model.Product;
import com.backend_yireh_store.domain.port.in.ProductUseCase;
import com.backend_yireh_store.domain.port.out.CategoryRepositoryPort;
import com.backend_yireh_store.domain.port.out.ProductRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService implements ProductUseCase {

    private final ProductRepositoryPort productRepository;
    private final CategoryRepositoryPort categoryRepository;

    public ProductService(ProductRepositoryPort productRepository,
                          CategoryRepositoryPort categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Product create(String name, BigDecimal price, String description, String imageUrl,
                          Long categoryId, int order) {
        if (productRepository.existsByNameIgnoreCase(name)) {
            throw new ProductAlreadyExistsException(name);
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        Product product = Product.createNew(name, price, description, imageUrl, category, order);
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getActive() {
        return productRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getActiveByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchByName(String query) {
        return productRepository.searchByName(query);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchByNameAndCategory(String query, Long categoryId) {
        return productRepository.searchByNameAndCategory(query, categoryId);
    }

    @Override
    @Transactional
    public Product update(Long id, String name, BigDecimal price, String description,
                          String imageUrl, Long categoryId, Integer order) {
        Product product = getById(id);
        if (name != null && !name.equalsIgnoreCase(product.getName())) {
            if (productRepository.existsByNameIgnoreCase(name)) {
                throw new ProductAlreadyExistsException(name);
            }
        }
        Category category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        }
        product.update(name, price, description, imageUrl, category, order);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product deactivate(Long id) {
        Product product = getById(id);
        product.deactivate();
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product activate(Long id) {
        Product product = getById(id);
        product.activate();
        return productRepository.save(product);
    }
}