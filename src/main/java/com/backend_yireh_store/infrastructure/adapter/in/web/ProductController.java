package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.Category;
import com.backend_yireh_store.domain.model.Product;
import com.backend_yireh_store.domain.port.in.CategoryUseCase;
import com.backend_yireh_store.domain.port.in.ProductUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CategoryResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.ProductRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.ProductResponse;
import com.backend_yireh_store.infrastructure.mapper.CategoryMapper;
import com.backend_yireh_store.infrastructure.mapper.ProductMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductUseCase productUseCase;
    private final CategoryUseCase categoryUseCase;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    public ProductController(ProductUseCase productUseCase,
                             CategoryUseCase categoryUseCase,
                             ProductMapper productMapper,
                             CategoryMapper categoryMapper) {
        this.productUseCase = productUseCase;
        this.categoryUseCase = categoryUseCase;
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(
            @RequestParam(defaultValue = "false") boolean activeOnly,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String query) {
        List<Product> products;
        if (query != null && !query.isEmpty()) {
            products = categoryId != null
                    ? productUseCase.searchByNameAndCategory(query, categoryId)
                    : productUseCase.searchByName(query);
        } else if (categoryId != null) {
            products = activeOnly ? productUseCase.getActiveByCategory(categoryId) : productUseCase.getByCategory(categoryId);
        } else {
            products = activeOnly ? productUseCase.getActive() : productUseCase.getAll();
        }
        return ResponseEntity.ok(products.stream().map(productMapper::toResponse).toList());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @RequestParam(defaultValue = "true") boolean activeOnly) {
        return ResponseEntity.ok(categoryUseCase.getActive().stream()
                .map(categoryMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productMapper.toResponse(productUseCase.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest req) {
        Product product = productUseCase.create(req.name(), req.price(), req.description(),
                req.imageUrl(), req.categoryId(), req.order());
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toResponse(product));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ProductRequest req) {
        Product product = productUseCase.update(id, req.name(), req.price(), req.description(),
                req.imageUrl(), req.categoryId(), req.order());
        return ResponseEntity.ok(productMapper.toResponse(product));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(productMapper.toResponse(productUseCase.deactivate(id)));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(productMapper.toResponse(productUseCase.activate(id)));
    }
}