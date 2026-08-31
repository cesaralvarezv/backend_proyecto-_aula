package com.backend_yireh_store.domain.exception;

public class CategoryAlreadyExistsException extends DomainException {
    public CategoryAlreadyExistsException(String name) {
        super("Ya existe una categoria con el nombre: " + name);
    }
}
