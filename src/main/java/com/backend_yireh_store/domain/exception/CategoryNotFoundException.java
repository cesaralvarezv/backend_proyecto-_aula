package com.backend_yireh_store.domain.exception;

public class CategoryNotFoundException extends DomainException {
    public CategoryNotFoundException(Long id) {
        super("Categoria no encontrada con id: " + id);
    }
}
