package com.backend_yireh_store.domain.exception;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(Long id) {
        super("Producto no encontrado con id: " + id);
    }
}