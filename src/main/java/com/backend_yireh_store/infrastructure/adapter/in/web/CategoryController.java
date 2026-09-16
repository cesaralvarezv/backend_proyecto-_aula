package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.Category;
import com.backend_yireh_store.domain.port.in.CategoryUseCase;
import com.backend_yireh_store.domain.port.in.ProductUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CategoryRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CategoryResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.ProductResponse;
import com.backend_yireh_store.infrastructure.mapper.CategoryMapper;
import com.backend_yireh_store.infrastructure.mapper.ProductMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryUseCase categoryUseCase;
    private final ProductUseCase productUseCase;
    private final CategoryMapper mapper;
    private final ProductMapper productMapper;

    public CategoryController(CategoryUseCase categoryUseCase,
                              ProductUseCase productUseCase,
                              CategoryMapper mapper,
                              ProductMapper productMapper) {
        this.categoryUseCase = categoryUseCase;
        this.productUseCase = productUseCase;
        this.mapper = mapper;
        this.productMapper = productMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(
            @RequestParam(defaultValue = "false") boolean activeOnly,
            @RequestParam(required = false) String location) {
        List<Category> categories;
        if (location != null && !location.isEmpty()) {
            categories = categoryUseCase.getActiveByLocation(location);
        } else {
            categories = activeOnly ? categoryUseCase.getActive() : categoryUseCase.getAll();
        }
        return ResponseEntity.ok(categories.stream().map(mapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(categoryUseCase.getById(id)));
    }

    /**
     * Catálogo público autenticado: productos activos que el usuario puede elegir
     * después de abrir una categoría en el inicio de la aplicación.
     */
    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductResponse>> getActiveProducts(@PathVariable Long id) {
        categoryUseCase.getById(id);
        return ResponseEntity.ok(productUseCase.getActiveByCategory(id).stream()
                .map(productMapper::toResponse)
                .toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest req) {
        Category category = categoryUseCase.create(req.name(), req.description(), req.icon(), req.location(), req.order());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(category));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody CategoryRequest req) {
        Category category = categoryUseCase.update(id, req.name(), req.description(), req.icon(), req.location(), req.order());
        return ResponseEntity.ok(mapper.toResponse(category));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(categoryUseCase.deactivate(id)));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(categoryUseCase.activate(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
