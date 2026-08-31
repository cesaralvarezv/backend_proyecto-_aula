package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.Category;
import com.backend_yireh_store.domain.port.in.CategoryUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CategoryRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CategoryResponse;
import com.backend_yireh_store.infrastructure.mapper.CategoryMapper;
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
    private final CategoryMapper mapper;

    public CategoryController(CategoryUseCase categoryUseCase, CategoryMapper mapper) {
        this.categoryUseCase = categoryUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        List<Category> categories = activeOnly ? categoryUseCase.getActive() : categoryUseCase.getAll();
        return ResponseEntity.ok(categories.stream().map(mapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(categoryUseCase.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest req) {
        Category category = categoryUseCase.create(req.name(), req.description(), req.icon(), req.order());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(category));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody CategoryRequest req) {
        Category category = categoryUseCase.update(id, req.name(), req.description(), req.icon(), req.order());
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